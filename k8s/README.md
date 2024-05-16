# Server Admin Kubernetes

This document describes a way to deploy the `Server Admin` application stack into a Minikube Kubernetes installation.

### Tools and dependencies

This how-to requires you to have the following dependencies installed:

- Latest version of the `minikube` binary (get it here: [https://minikube.sigs.k8s.io/docs/start/](https://minikube.sigs.k8s.io/docs/start/))
- A `minikube` Kubernetes cluster instance (follow step 2 from here: [https://minikube.sigs.k8s.io/docs/start/](https://minikube.sigs.k8s.io/docs/start/))
- `kubectl` binary (get it here: [https://kubernetes.io/docs/tasks/tools/](https://kubernetes.io/docs/tasks/tools/))
- `kustomize` binary (get it here: [https://kubectl.docs.kubernetes.io/installation/kustomize/binaries/](https://kubectl.docs.kubernetes.io/installation/kustomize/binaries/))
- A VM with a Linux system on it (Debian 12 is tested to work)
- `openjdk-21` installed on the Linux system (to build the packages)
- Docker engine installed

To install `openjdk-21` on Debian 12, you will need to enable the `testing` repositories:

```bash
$ sudo echo 'deb http://deb.debian.org/debian trixie main' >> /etc/apt/sources.list
$ apt update
$ apt install openjdk-21-jdk-headless/testing
```

Confirm ‘yes’ to the question of installing new and replacing existing packages.

After this you can test your `jdk` version by running:

```bash
$ java --version
openjdk 21.0.3 2024-04-16
OpenJDK Runtime Environment (build 21.0.3+9-Debian-2)
OpenJDK 64-Bit Server VM (build 21.0.3+9-Debian-2, mixed mode, sharing)
```

## Building

We first need to build the Kotlin applications. To do so run the following command in the root of this repository:

```bash
$ ./gradlew clean build
Starting a Gradle Daemon (subsequent builds will be faster)
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.7/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 47s
46 actionable tasks: 46 executed
```

After the build is done, the server and client application `.jar` packages can be located in `apps/sa-server/build/libs/sa-server.jar` and `apps/sa-client/build/libs/sa-client.jar` respectively.

After the `.jar` packages are built we can continue to package the applications into Docker containers.

In order to simplify the procedure, there are build scripts available in `docker/apps/sa-server/build.sh` and `docker/apps/sa-client/build.sh`.

Run both to build the docker images:

```bash
$ cd docker/apps/sa-server/
$ sh build.sh
+ JARPATH=../../../apps/sa-server/build/libs/sa-server.jar
+ [[ ! -e ../../../apps/sa-server/build/libs/sa-server.jar ]]
build.sh: 9: [[: not found
+ cp ../../../apps/sa-server/build/libs/sa-server.jar .
+ docker build --tag sa-server:0.0.1 .
Sending build context to Docker daemon  69.09MB
Step 1/4 : FROM eclipse-temurin:21-jdk-alpine
 ---> 344020953d26
Step 2/4 : COPY sa-server.jar app.jar
 ---> f3e69d64f3c9
Step 3/4 : COPY application.yaml application.yaml
 ---> ed0e7daf72f8
Step 4/4 : ENTRYPOINT ["java","-jar","app.jar"]
 ---> Running in 748881d6a979
Removing intermediate container 748881d6a979
 ---> 5d5826f2d13a
Successfully built 5d5826f2d13a
Successfully tagged sa-server:0.0.1
+ minikube image load sa-server:0.0.1

# check if the image is uploaded
$ minikube image ls |grep sa-server
docker.io/library/sa-server:0.0.1
```

Repeat the above for `sa-client`:

```bash
$ cd docker/apps/sa-client/
$ sh build.sh
+ JARPATH=../../../apps/sa-client/build/libs/sa-client.jar
+ cp ../../../apps/sa-client/build/libs/sa-client.jar .
+ docker build --tag sa-client:0.0.1 .
Sending build context to Docker daemon  81.72MB
Step 1/4 : FROM eclipse-temurin:21-jdk-alpine
 ---> 344020953d26
Step 2/4 : COPY sa-client.jar app.jar
 ---> c4a268652c13
Step 3/4 : COPY application.yaml application.yaml
 ---> 3bed9a78bab8
Step 4/4 : ENTRYPOINT ["java","-jar","app.jar"]
 ---> Running in 44d7a9ebfb9d
Removing intermediate container 44d7a9ebfb9d
 ---> 6059c5235149
Successfully built 6059c5235149
Successfully tagged sa-client:0.0.1
+ minikube image load sa-client:0.0.1

# check if the image is uploaded
$ minikube image ls |grep sa-client
docker.io/library/sa-client:0.0.1
```

## Deploying

After we’re done with building and packaging of the applications, we can deploy the stack.

Deployment is done with the help of `kustomize` (Kubernetes native configuration management).

The deployment is done in two steps:

1. Installing dependency CRDs
2. Deploying the full infrastructure and application stack

The `kustomize` definitions are located in the `k8s` directory in the root of the repository. Here is a listing of all the definitions:

```bash
$ tree k8s/
k8s/
├── app
│   ├── client-pvc.yaml
│   ├── client.yaml
│   ├── kustomization.yaml
│   ├── namespace.yaml
│   └── server.yaml
├── crds
│   ├── kustomization.yaml
│   └── strimzi
│       ├── kustomization.yaml
│       └── namespace.yaml
├── dev
│   └── kustomization.yaml
├── kafka
│   ├── deployment.yaml
│   └── kustomization.yaml
└── postgres
    ├── data-pvc.yaml
    ├── deployment.yaml
    ├── kustomization.yaml
    ├── namespace.yaml
    └── service.yaml

7 directories, 16 files

```

The stack is created in the following way:

1. Kafka cluster with 3 nodes (using Kraft)
2. A single Postgresql instance
3. Server and Client applications 

### 1. Installing CRDs

To install the dependency (ATM only the Strimzi Kafka operator), go to the `k8s` directory and execute the following:

```bash
$ cd k8s
$ kubectl apply -k crds/
namespace/kafka created
customresourcedefinition.apiextensions.k8s.io/kafkabridges.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkaconnectors.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkaconnects.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkamirrormaker2s.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkamirrormakers.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkanodepools.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkarebalances.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkas.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkatopics.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/kafkausers.kafka.strimzi.io created
customresourcedefinition.apiextensions.k8s.io/strimzipodsets.core.strimzi.io created
serviceaccount/strimzi-cluster-operator created
clusterrole.rbac.authorization.k8s.io/strimzi-cluster-operator-global created
clusterrole.rbac.authorization.k8s.io/strimzi-cluster-operator-leader-election created
clusterrole.rbac.authorization.k8s.io/strimzi-cluster-operator-namespaced created
clusterrole.rbac.authorization.k8s.io/strimzi-cluster-operator-watched created
clusterrole.rbac.authorization.k8s.io/strimzi-entity-operator created
clusterrole.rbac.authorization.k8s.io/strimzi-kafka-broker created
clusterrole.rbac.authorization.k8s.io/strimzi-kafka-client created
rolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator created
rolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator-entity-operator-delegation created
rolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator-leader-election created
rolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator-watched created
clusterrolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator created
clusterrolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator-kafka-broker-delegation created
clusterrolebinding.rbac.authorization.k8s.io/strimzi-cluster-operator-kafka-client-delegation created
configmap/strimzi-cluster-operator created
deployment.apps/strimzi-cluster-operator created

# wait until the resources are ready
$ kubectl get pod -n kafka --watch
NAME                                       READY   STATUS    RESTARTS   AGE
strimzi-cluster-operator-b9c59999f-5jtgj   0/1     Running   0          9s
strimzi-cluster-operator-b9c59999f-5jtgj   1/1     Running   0          31s
```

This step installs new Strimzi Kafka resources that we will use to setup our Kafka cluster.

### 2. Deploying infrastructure and application stack

To install the infrastructure and application stack, go to the `k8s` directory and execute the following:

```bash
$ kubectl apply -k dev/
namespace/apps created
namespace/postgres created
configmap/appenv-2gkg7kttf5 created
configmap/pgconfig-mmh46ffb67 created
secret/pgcreds-g7c9h9c6df created
service/postgres created
persistentvolumeclaim/postgres-data created
persistentvolumeclaim/saclient-data created
deployment.apps/postgres created
deployment.apps/sa-client created
deployment.apps/sa-server created
kafka.kafka.strimzi.io/openr created
kafkanodepool.kafka.strimzi.io/openr created

# check if kafka is deployed
$ kubectl get pod -n kafka
NAME                                       READY   STATUS    RESTARTS   AGE
openr-entity-operator-596577f778-d9tgp     2/2     Running   0          73s
openr-openr-0                              1/1     Running   0          100s
openr-openr-1                              1/1     Running   0          100s
openr-openr-2                              1/1     Running   0          99s
strimzi-cluster-operator-b9c59999f-5jtgj   1/1     Running   0          3m45s

# check if postgresql and apps are deployed
$ kubectl get pod -n default -o wide
NAME                         READY   STATUS    RESTARTS        AGE     IP             NODE       NOMINATED NODE   READINESS GATES
dnsutils                     1/1     Running   1 (4h14m ago)   4h40m   10.244.0.159   minikube   <none>           <none>
postgres-6db5cdbfdc-q6hrp    1/1     Running   0               3m2s    10.244.0.181   minikube   <none>           <none>
sa-client-66cccb5999-9cg9x   1/1     Running   0               3m2s    10.244.0.179   minikube   <none>           <none>
sa-server-5b96f76d78-f6pl5   1/1     Running   0               3m2s    10.244.0.180   minikube   <none>           <none>
```

To check if the stack is working properly, you can check the logs of the `server` and `client` applications.

For example:

```bash
$ kubectl logs --since=10s sa-server-5b96f76d78-f6pl5
2024-05-16T17:59:19.128Z -TRACE [        kafka-1] d.mbo.serveradmin.server.ServerListener  : received record {}
2024-05-16T17:59:19.129Z -TRACE [        kafka-1] d.mbo.serveradmin.server.ServerListener  : targetKey: 5d5cecbe-f37a-4070-9e36-87504c58d408 {record.targetKey=5d5cecbe-f37a-4070-9e36-87504c58d408, record.topic=sa-board-to-ground.heartbeat-message, record.type=heartbeat-message, record.sender=sa-client1, record.senderKey=d0841578-a56c-431e-aa76-77ec907e1d84, record.contentType=NONE, record.schemaVersion=1, record.id=385a83a8-ada3-4ad2-88cb-455c916c147f}
2024-05-16T17:59:19.130Z -TRACE [        kafka-1] d.mbo.serveradmin.server.ServerListener  : using processor dev.mbo.serveradmin.server.heartbeat.HeartbeatMessageProcessor {record.targetKey=5d5cecbe-f37a-4070-9e36-87504c58d408, record.topic=sa-board-to-ground.heartbeat-message, record.type=heartbeat-message, record.sender=sa-client1, record.senderKey=d0841578-a56c-431e-aa76-77ec907e1d84, record.contentType=NONE, record.ts=2024-05-16T17:59:19.108Z, record.schemaVersion=1, record.id=385a83a8-ada3-4ad2-88cb-455c916c147f}
2024-05-16T17:59:19.131Z -DEBUG [        kafka-1] d.m.s.s.h.HeartbeatMessageProcessor      : processing RecordStaticMetadata(type=heartbeat-message, schemaVersion=1, contentType=NONE) message from sa-client1(d0841578-a56c-431e-aa76-77ec907e1d84) created at 2024-05-16T17:59:19.108Z with headers {id=385a83a8-ada3-4ad2-88cb-455c916c147f, type=heartbeat-message, schema_version=1, Content-Type=NONE, sender_key=d0841578-a56c-431e-aa76-77ec907e1d84, target_key=5d5cecbe-f37a-4070-9e36-87504c58d408}: null {record.targetKey=5d5cecbe-f37a-4070-9e36-87504c58d408, record.topic=sa-board-to-ground.heartbeat-message, record.type=heartbeat-message, record.sender=sa-client1, record.senderKey=d0841578-a56c-431e-aa76-77ec907e1d84, record.contentType=NONE, record.ts=2024-05-16T17:59:19.108Z, record.schemaVersion=1, record.id=385a83a8-ada3-4ad2-88cb-455c916c147f}
2024-05-16T17:59:19.148Z -DEBUG [        kafka-1] d.m.s.server.db.client.ClientService     : updated client.lastHeartbeat {record.targetKey=5d5cecbe-f37a-4070-9e36-87504c58d408, record.topic=sa-board-to-ground.heartbeat-message, record.type=heartbeat-message, record.sender=sa-client1, record.senderKey=d0841578-a56c-431e-aa76-77ec907e1d84, record.contentType=NONE, record.ts=2024-05-16T17:59:19.108Z, record.schemaVersion=1, record.id=385a83a8-ada3-4ad2-88cb-455c916c147f}
2024-05-16T17:59:19.158Z -TRACE [        kafka-1] d.mbo.serveradmin.server.ServerListener  : successfully processed record {record.targetKey=5d5cecbe-f37a-4070-9e36-87504c58d408, record.topic=sa-board-to-ground.heartbeat-message, record.type=heartbeat-message, record.sender=sa-client1, record.senderKey=d0841578-a56c-431e-aa76-77ec907e1d84, record.contentType=NONE, record.ts=2024-05-16T17:59:19.108Z, record.schemaVersion=1, record.id=385a83a8-ada3-4ad2-88cb-455c916c147f}

$ kubectl logs --since=30s sa-client-66cccb5999-9cg9x
2024-05-16T17:59:29.104Z -DEBUG [  scheduling-29] d.m.s.client.heartbeat.HeartbeatTask     : sending heartbeat {}
2024-05-16T17:59:29.105Z -TRACE [  scheduling-29] d.m.s.messaging.sender.MessageSender     : sent message as sa-client1 to topic: sa-board-to-ground.heartbeat-message {}
2024-05-16T17:59:39.104Z -DEBUG [  scheduling-30] d.m.s.client.heartbeat.HeartbeatTask     : sending heartbeat {}
2024-05-16T17:59:39.105Z -TRACE [  scheduling-30] d.m.s.messaging.sender.MessageSender     : sent message as sa-client1 to topic: sa-board-to-ground.heartbeat-message {}
2024-05-16T17:59:49.104Z -DEBUG [  scheduling-31] d.m.s.client.heartbeat.HeartbeatTask     : sending heartbeat {}
2024-05-16T17:59:49.105Z -TRACE [  scheduling-31] d.m.s.messaging.sender.MessageSender     : sent message as sa-client1 to topic: sa-board-to-ground.heartbeat-message {}
```
