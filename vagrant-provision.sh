sudo apt-get update
sudo apt-get install openjdk-7-jdk
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

# zookeeper setup
wget http://mirror.symnds.com/software/Apache/zookeeper/stable/zookeeper-3.4.5.tar.gz
tar xf zookeeper-3.4.5.tar.gz

# worst config of all time, don't do this...
sudo mkdir /var/zookeeper
sudo chmod 777 /var/zookeeper
cp /vargrant/zoo.cf ./zookeeper-3.4.5/conf/

./zookeeper-3.4.5/bin/zkServer.sh start

# zmq setup
wget http://download.zeromq.org/zeromq-2.1.7.tar.gz
tar -xf zeromq-2.1.7.tar.gz 

sudo apt-get install libtool
sudo apt-get install autoconf
sudo apt-get install automake
sudo apt-get install uuid-dev

sudo apt-get install g++
sudo apt-get install make

cd zeromq-2.1.7
./configure
sudo make install
sudo ldconfig
cd ..

# jzmq setup
sudo apt-get install git
git clone https://github.com/nathanmarz/jzmq.git
sudo apt-get install pkg-config
cd jzmq

touch src/classdist_noinst.stamp

./autogen.sh
./configure --prefix=/usr CFLAGS="-g -O2" LDFLAGS="-Wl,--as-needed -Wl,-z,defs"

cd src

CLASSPATH=.:./.:$CLASSPATH javac -d . org/zeromq/ZMQ.java org/zeromq/ZMQException.java org/zeromq/ZMQQueue.java org/zeromq/ZMQForwarder.java org/zeromq/ZMQStreamer.java
cd ..

sudo make install

sudo apt-get install unzip

cd ..

# storm setup (finally...)

wget https://github.com/downloads/nathanmarz/storm/storm-0.8.1.zip

unzip storm-0.8.1.zip 

cd storm-0.8.1.zip
sudo mkdir /mnt/storm
sudo chmod 777 /mnt/storm

cp /vagrant/storm.yaml ./conf

