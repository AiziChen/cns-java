# cns-java
CNS in Java Programming Language

## Build
### build requirements
* JDK 1.8 or larger
* Maven 3
* this repository

### How to build
1. clone this repository in your computer
2. install `SObj-java` library to your local maven repositories:
```shell
cd cns-java/SObj-java
mvn install
```
3. build it:
```shell
mvn package
```
4. after above steps finished, jars files can find in the `target` folder]


## Configuration
configuration arguments:
``` scheme
(*obj
  (proxyKey "Meng")
  (udpFlag "httpUDP")
  (listenPorts (*list 1080 520))
  (password "quanyec")
  (enableDnsOverUdp #t)
  (enableHttpDns #t))
```
all the above configurations are written in `config.sobj` file on project folder.
if you do not have `config.sobj` file on your project folder, then `cns-java` program will load the default
configuration file `default-config.sobj` in project's `src/main/resources` folder.

## Running on background
if your computer system based on `linux`, then type it:
```shell
setsid nohup target/cns-java-xxx.jar > /dev/null
```
run `top` or `htop` command, if any errors do not occurred
, `cns-java` will appear on the process list. 