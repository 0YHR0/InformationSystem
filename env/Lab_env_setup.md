## Docker

```sh
# remove old versions
sudo apt-get remove docker docker-engine docker.io containerd runc

# Set up the repository
sudo apt-get update

sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
    
# Add Docker’s official GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# set up the stable repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  
# Install Docker Engine
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

sudo service docker start
# Verify
sudo docker run hello-world

#check the version
sudo docker -v
    
    
```



## K8s

```sh
# Install Kubernetes Servers
sudo apt update
sudo apt -y full-upgrade
[ -f /var/run/reboot-required ] && sudo reboot -f

# Install kubelet, kubeadm and kubectl
sudo apt -y install curl apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list

# sudo apt update
sudo apt -y install vim git curl wget kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl

# Confirm installation by checking the version of kubectl
kubectl version --client && kubeadm version

# Disable Swap
sudo sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab
sudo swapoff -a

# Enable kernel modules and configure sysctl
# Enable kernel modules
sudo modprobe overlay
sudo modprobe br_netfilter

# Add some settings to sysctl
sudo tee /etc/sysctl.d/kubernetes.conf<<EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF

# Reload sysctl
sudo sysctl --system

# Installing Docker runtime
# Add repo and Install packages
sudo apt update
sudo apt install -y curl gnupg2 software-properties-common apt-transport-https ca-certificates
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install -y containerd.io docker-ce docker-ce-cli

# Create required directories
sudo mkdir -p /etc/systemd/system/docker.service.d

# Create daemon json config file
sudo tee /etc/docker/daemon.json <<EOF
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "storage-driver": "overlay2"
}
EOF

# Start and enable Services
sudo systemctl daemon-reload 
sudo systemctl restart docker
sudo systemctl enable docker

# set docker Cgroup Driver as systemd
sed -i "s#^ExecStart=/usr/bin/dockerd.*#ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd#g" /usr/lib/systemd/system/docker.service

# start the k8s
systemctl enable kubelet && systemctl start kubelet

# only do the following in the master node
# make sure that the br_netfilter module is loaded
lsmod | grep br_netfilter


sudo vim /etc/hosts
# add the line to the file:129.69.209.197    k8s.master
# add all the node and its address


```



init the master node: run this shell

```shell
#!/bin/bash

touch ./kubeadm-config.yaml
rm /etc/containerd/config.toml
systemctl restart containerd
sudo kubeadm init --pod-network-cidr=192.168.0.0/16

# config kubectl
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl create -f https://projectcalico.docs.tigera.io/manifests/tigera-operator.yaml
kubectl create -f https://projectcalico.docs.tigera.io/manifests/custom-resources.yaml
watch kubectl get pods -n calico-system
```



init worker:

```sh
export MASTER_IP=129.69.209.197
export APISERVER_NAME=k8s.master
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
kubeadm join 129.69.209.197:6443 --token 45khez.wjsn5vq3ik0n7jld --discovery-token-ca-cert-hash sha256:6140975ee6b033c5d4a570b2930d0d348dffa4ea5c7384f2a1741d72123759e2 
```



Common problem:

![image-20220512022214695](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220512022214695.png)



Success!

![image-20220512022923159](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220512022923159.png)



Test:Nginx

```sh
kubectl get svc -ns -A -o wide
```



In the results you can find niginx is depolyed on the vm3 

enter 

http://129.69.209.200:31571/ or

http://129.69.209.199:31571/ 

http://129.69.209.198:31571/ 

http://129.69.209.197:31571/ 

 in your browser 

or use: 

```sh
curl 129.69.209.197:31571 
curl 129.69.209.198:31571 
curl 129.69.209.199:31571 
curl 129.69.209.200:31571 
```



You can find the nginx welcome page![image-20220512224339983](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220512224339983.png)



## K8s Metrics Server

```sh
wget https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

but the http can not connect so config the components.yaml

```yaml
add --kubelet-insecure-tls
template:
metadata:
labels:
k8s-app: metrics-server
spec:
containers:
- args:
- --cert-dir=/tmp
- --secure-port=4443
- --kubelet-preferred-address-types=InternalIP,ExternalIP,Hostname
- --kubelet-use-node-status-port
- --metric-resolution=15s
- --kubelet-insecure-tls
```

```sh
kubectl apply -f components.yaml
```

![image-20220512230024689](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220512230024689.png)

![image-20220512230038014](https://markdown-1301334775.cos.eu-frankfurt.myqcloud.com/image-20220512230038014.png)



When deployed the dashboard on k8s v1.24.0, the token can not be generated. And I have reported the issue on github.
