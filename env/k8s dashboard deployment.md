# k8s dashboard deployment





Get the yaml file:

```sh
wget https://raw.githubusercontent.com/kubernetes/dashboard/v2.5.0/aio/deploy/recommended.yaml
```





Edit the yaml file to make it exposed in port 30000

```sh
vim recommended.yaml
```

```yaml
---
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  type: NodePort #增加
  ports:
    - port: 443
      targetPort: 8443
      nodePort: 30000 #增加
  selector:
    k8s-app: kubernetes-dashboard
---
```

```sh
kubectl apply -f recommand.yaml
kubectl get pods -ns -A -o wide
```

![image-20220618232915767](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220618232915767.png)

You can use the dashboard in the browser(Please make sure you connect to the vpn of informatik)

https://129.69.209.199:30000/#/login





create the user:

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
```



binding the user:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
```





Getting the token:

```sh
kubectl -n kubernetes-dashboard create token admin-user
```



token:

```sh
eyJhbGciOiJSUzI1NiIsImtpZCI6IjNJVXYzLTU5c2JabmVrdnpMNTJGeEFWTlBTS2dUR2tEdG9zYUpPd3A1cjgifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNjU1NTkxNTM0LCJpYXQiOjE2NTU1ODc5MzQsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsInNlcnZpY2VhY2NvdW50Ijp7Im5hbWUiOiJhZG1pbi11c2VyIiwidWlkIjoiMzMwY2Q3NDYtYTZjOC00ZjZlLTkxYjAtZDU3MjdlZWFjZjFmIn19LCJuYmYiOjE2NTU1ODc5MzQsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlcm5ldGVzLWRhc2hib2FyZDphZG1pbi11c2VyIn0.QA7jBFJGVPQv81wsVBcA9zJvw0eYGkuM82M04NBs7SOMh0FFeRAXcrS0eNIXJqkP16QRwDnqbvhVzazAWoMobx90ntAWMy4mmpeUikGF4ov3YbZW9q6prGhyq_v1FVZQNllb7YqYMreGhFKWCC7hJTPRMf3cYm7YETFyElprlQ1Qma0UhrHzmbLpsU35upwP5gs5zwDuLzH4bnIxk-p86gQw7hFBPOfGKikZo3z33lpb8LxjDzW5WtHFb9WVRBMXtbfya4_r9M5vs03Il7NWTx-p5Ehpyd2I5DF9yFZ8bfp1MtU2QW1zVr4S-yTrQnxD0BFG25uLXBxoh0Jt0NHAJA

```



success! Make sure to connect with informatik vpn!

![image-20220618233314688](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220618233314688.png)





ref：https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/