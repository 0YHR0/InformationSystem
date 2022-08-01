# Deployment of solr cloud



reference: https://apache.github.io/solr-operator/docs/local_tutorial#install-the-solr-operator

### Install an Ingress Controller

```sh
# Install the nginx ingress controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud/deploy.yaml

# Inspect that the ingress controller is running by visiting the Kubernetes dashboard 
# and selecting namespace `ingress-nginx`, or running this command:
kubectl get all --namespace ingress-nginx

# Edit your /etc/hosts file (`sudo vi /etc/hosts`) and replace the 127.0.0.1 line with:
127.0.0.1	localhost default-example-solrcloud.ing.local.domain ing.local.domain default-example-solrcloud-0.ing.local.domain default-example-solrcloud-1.ing.local.domain default-example-solrcloud-2.ing.local.domain dinghy-ping.localhost
```



### Install Helm on the master node

reference: https://github.com/helm/helm/releases

```sh
mkdir myhelm
cd myhelm
curl -SLO https://get.helm.sh/helm-v3.7.1-linux-amd64.tar.gz
tar -zxvf  helm-v3.7.1-linux-amd64.tar.gz
mv  linux-amd64/helm  /usr/local/bin/helm
helm version
```



### Install the Solr Operator

```sh
$ helm repo add apache-solr https://solr.apache.org/charts
$ helm repo update
# Install the Solr & Zookeeper CRDs
$ kubectl create -f https://solr.apache.org/operator/downloads/crds/v0.5.1/all-with-dependencies.yaml
# Install the Solr operator and Zookeeper Operator
$ helm install solr-operator apache-solr/solr-operator --version 0.5.1
$ kubectl get all -ns -A | grep solr
```

![image-20220613165104359](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220613165104359.png)



### delete the cluster

```sh
helm delete example-solr apache-solr/solr
```



### use the yaml file to deploy

```yaml
apiVersion: solr.apache.org/v1beta1
kind: SolrCloud
metadata:
  name: example
spec:
  replicas: 4
  solrImage:
    tag: 8.1.1
```

1. change the common service from Cluster port to NodePort to make it accessable from the outside

   ```sh
   kubectl edit svc example-solrcloud-common
   ```

   ![Image](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/solr_cloud_common_service.PNG)

2. Finally we can access by http://129.69.209.197:30002/solr/#/~cloud?view=nodes