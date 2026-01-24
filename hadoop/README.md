#### Run Hadoop 🐘
```bash
ubuntu@ubuntu:~/hadoop/etc/hadoop$ hdfs namenode -format
ubuntu@ubuntu:~/hadoop/etc/hadoop$ start-dfs.sh
ubuntu@ubuntu:~/hadoop/etc/hadoop$ start-yarn.sh
```
```bash
ubuntu@ubuntu:~/hadoop/etc/hadoop$ jps
11776 SecondaryNameNode
12304 Jps
12162 NodeManager
11411 NameNode
12024 ResourceManager
11546 DataNode
```