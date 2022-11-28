# NFS deployment

ref: https://phoenixnap.com/kb/ubuntu-nfs-server



### 1. in the host（vm1）：

```sh
#install the server
$ apt update && sudo apt upgrade -y
$ sudo apt-get install nfs-kernel-server nfs-common -y
$ sudo mkdir -p /mnt/nfs-solr #create the share directory
$ sudo chown nobody:nogroup /mnt/nfs-solr #make it public
$ sudo chmod 777 /mnt/nfs-solr
#Set permissions to 777, so everyone can read, write, and execute files in this folder
$ sudo vi /etc/exports
#in the export file, add these lines to grant access to each client
(/mnt/nfs-solr clientIP(rw,sync,no_subtree_check))
/mnt/nfsdir *(rw,sync,insecure,no_subtree_check,no_root_squash)
/mnt/nfsdir *(rw,sync,insecure,no_subtree_check,no_root_squash)
/mnt/nfsdir *(rw,sync,insecure,no_subtree_check,no_root_squash)
```

![image-20220801212551613](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220801212551613.png)

+ “rw” option provides clients with read and write access to directories on the server. 
+ “sync” forces NFS to write changes before responding to the client. This option ensures the state of the host is accurately presented to clients. 
+ “no_subtree_check” disables subtree checking. The subtree process may cause problems when users rename files.

```sh
$su
root$ exportfs -a #export shared directories registered in the export file
$exit
$ sudo systemctl restart nfs-kernel-server #restart the NFS Kernel Server to
```

apply the changes to configuration





### 2. in client vm2,vm3

```sh
$ sudo apt update
$ sudo apt install nfs-common
$sudo mkdir -p /mnt/nfssolr_client
$sudo mount 129.69.209.197:/mnt/nfs-solr /mnt/nfssolr_client
$ df -h #to check if mounted the folder successfully
```

![image-20220801212712690](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220801212712690.png)



**(for further use, to unmount, $sudo umount /mnt/nfsdir_client)**
