# Postgresql cluster



Admin UI:

http://129.69.209.197:30398/



create a cluster:

```yaml
kind: "postgresql"
apiVersion: "acid.zalan.do/v1"

metadata:
  name: "acid-postgres"
  namespace: "default"
  labels:
    team: acid
spec:
  teamId: "acid"
  postgresql:
    version: "14"
  numberOfInstances: 4
  enableMasterLoadBalancer: true
  enableReplicaLoadBalancer: true
  volume:
    size: "5Gi"
  users:
    postgres: 
    - superuser
  databases:
    Library: postgres
    test: postgres
  allowedSourceRanges:

  resources:
    requests:
      cpu: 100m
      memory: 100Mi
    limits:
      cpu: 500m
      memory: 500Mi
```



success:

![image-20220617235814924](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220617235814924.png)

![86024079fc17bdc01a078595b5e3406](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/86024079fc17bdc01a078595b5e3406.png)



>  But the cluster can not be accessed from the outside network, because the document and the solution on the Google is not so clear:![image-20220617235314224](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220617235314224.png)

> So I try to wrap the whole thing to a service:
>
> ```sh
> kubectl port-forward acid-postgresforsolr-0 5433:5432
> ```
>
> But still failed





## Connect from remote

![image-20220801202011456](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220801202011456.png)



### LB for master

Then I found a loadbalancer for master and a loadbalancer for replica on k8s created by postgres-operator, it listens on port 30414 and 31104

![image-20221004205802295](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20221004205802295.png)



### LB for replica

Lets take a look at the secret created by the operator, and use the secret to connect to the loadbalancer

![74675472203d15423cad17ca272eb00](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/74675472203d15423cad17ca272eb00.png)



## DO NOT CHANGE THE PASSWORD, OTHERWISE THE OPERATOR WOULD NOT GET THE NEW PASSWORD AND THROW AN EXCEPTION

we can use whatever tool we have from our own computer to connect to the postgresql cluster master by: 

+ 129.69.209.197:30414
+ username:postgres
+ password:XvR7XF6nbt7OjCjMEENiROPo9J3yqIk3CfT1AJHKXSPxrFANnTZ9A3N2bU8pW4jj

And the postgresql cluster master by: 

+ 129.69.209.197:31104
+ username:postgres
+ password:XvR7XF6nbt7OjCjMEENiROPo9J3yqIk3CfT1AJHKXSPxrFANnTZ9A3N2bU8pW4jj

Example:

![image-20221004205947969](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20221004205947969.png)



And the log of the cluster can be found on :http://129.69.209.197:30398/#/logs/default/acid-postgres

![image-20221004210025272](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20221004210025272.png)