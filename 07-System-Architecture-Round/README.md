# System Architecture Interview Preparation Guide

## 🎯 Overview
Comprehensive interview preparation for system architecture roles covering microservices, monoliths, distributed systems, cloud architecture, and modern architectural patterns with practical examples and implementation details.

## 🏗️ System Architecture Fundamentals

### **Q1: Design a microservices architecture**
**Answer**:
1. **Microservices Architecture Components**:
   - **API Gateway**: Single entry point for all client requests
   - **Service Discovery**: Dynamic service registration and discovery
   - **Load Balancer**: Distribute traffic across service instances
   - **Circuit Breaker**: Prevent cascade failures
   - **Distributed Tracing**: Monitor request flow across services
   - **Centralized Logging**: Aggregate logs from all services

2. **E-commerce Microservices Architecture**:
   ```yaml
   # Docker Compose for microservices
   version: '3.8'
   services:
     api-gateway:
       image: nginx:alpine
       ports:
         - "80:80"
       depends_on:
         - user-service
         - product-service
         - order-service
         - payment-service
       volumes:
         - ./nginx.conf:/etc/nginx/nginx.conf
     
     user-service:
       image: user-service:latest
       ports:
         - "8081:8080"
       environment:
         - DB_HOST=user-db
         - REDIS_HOST=redis
       depends_on:
         - user-db
         - redis
     
     product-service:
       image: product-service:latest
       ports:
         - "8082:8080"
       environment:
         - DB_HOST=product-db
         - CACHE_HOST=redis
       depends_on:
         - product-db
         - redis
     
     order-service:
       image: order-service:latest
       ports:
         - "8083:8080"
       environment:
         - DB_HOST=order-db
         - USER_SERVICE_URL=http://user-service:8080
         - PRODUCT_SERVICE_URL=http://product-service:8080
       depends_on:
         - order-db
         - user-service
         - product-service
     
     payment-service:
       image: payment-service:latest
       ports:
         - "8084:8080"
       environment:
         - DB_HOST=payment-db
         - STRIPE_SECRET_KEY=${STRIPE_SECRET_KEY}
       depends_on:
         - payment-db
   ```

3. **Service Communication Patterns**:
   ```python
   # Service-to-service communication with circuit breaker
   import requests
   from circuitbreaker import circuit
   
   class UserServiceClient:
       def __init__(self, base_url):
           self.base_url = base_url
           self.session = requests.Session()
       
       @circuit(failure_threshold=5, recovery_timeout=60)
       def get_user(self, user_id):
           response = self.session.get(f"{self.base_url}/users/{user_id}")
           response.raise_for_status()
           return response.json()
       
       @circuit(failure_threshold=5, recovery_timeout=60)
       def create_user(self, user_data):
           response = self.session.post(f"{self.base_url}/users", json=user_data)
           response.raise_for_status()
           return response.json()
   
   # Event-driven communication
   import json
   import redis
   
   class EventPublisher:
       def __init__(self, redis_client):
           self.redis_client = redis_client
       
       def publish_event(self, event_type, event_data):
           event = {
               'type': event_type,
               'data': event_data,
               'timestamp': time.time(),
               'id': str(uuid.uuid4())
           }
           self.redis_client.publish('events', json.dumps(event))
   
   class EventSubscriber:
       def __init__(self, redis_client, handlers):
           self.redis_client = redis_client
           self.handlers = handlers
       
       def start_listening(self):
           pubsub = self.redis_client.pubsub()
           pubsub.subscribe('events')
           
           for message in pubsub.listen():
               if message['type'] == 'message':
                   event = json.loads(message['data'])
                   handler = self.handlers.get(event['type'])
                   if handler:
                       handler(event['data'])
   ```

### **Q2: Design a monolithic to microservices migration strategy**
**Answer**:
1. **Migration Strategy**:
   - **Strangler Fig Pattern**: Gradually replace monolith with microservices
   - **Database Per Service**: Separate databases for each service
   - **API Gateway**: Route requests to appropriate services
   - **Event Sourcing**: Maintain data consistency across services
   - **CQRS**: Separate read and write operations

2. **Migration Implementation**:
   ```python
   # Strangler Fig Pattern Implementation
   class MigrationRouter:
       def __init__(self):
           self.microservices = {
               'users': 'http://user-service:8080',
               'products': 'http://product-service:8080',
               'orders': 'http://order-service:8080'
           }
           self.monolith_url = 'http://monolith:8080'
       
       def route_request(self, path, method, data=None):
           # Check if service has been migrated
           service = self.get_service_from_path(path)
           
           if service in self.microservices:
               # Route to microservice
               return self.call_microservice(service, path, method, data)
           else:
               # Route to monolith
               return self.call_monolith(path, method, data)
       
       def get_service_from_path(self, path):
           if path.startswith('/users'):
               return 'users'
           elif path.startswith('/products'):
               return 'products'
           elif path.startswith('/orders'):
               return 'orders'
           return None
   ```

3. **Database Migration Strategy**:
   ```sql
   -- Shared database with service-specific schemas
   CREATE SCHEMA user_service;
   CREATE SCHEMA product_service;
   CREATE SCHEMA order_service;
   
   -- User service tables
   CREATE TABLE user_service.users (
       id SERIAL PRIMARY KEY,
       email VARCHAR(255) UNIQUE NOT NULL,
       first_name VARCHAR(100),
       last_name VARCHAR(100),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   
   -- Product service tables
   CREATE TABLE product_service.products (
       id SERIAL PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       description TEXT,
       price DECIMAL(10,2) NOT NULL,
       category_id INTEGER,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   
   -- Order service tables
   CREATE TABLE order_service.orders (
       id SERIAL PRIMARY KEY,
       user_id INTEGER NOT NULL,
       total_amount DECIMAL(10,2) NOT NULL,
       status VARCHAR(20) DEFAULT 'pending',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

### **Q3: Design a distributed system architecture**
**Answer**:
1. **Distributed System Components**:
   - **Load Balancer**: Distribute traffic across multiple nodes
   - **Service Discovery**: Dynamic service registration
   - **Message Queue**: Asynchronous communication
   - **Distributed Cache**: Shared caching layer
   - **Distributed Database**: Data partitioning and replication
   - **Monitoring**: Centralized observability

2. **Distributed System Implementation**:
   ```python
   # Service Discovery with Consul
   import consul
   import requests
   
   class ServiceDiscovery:
       def __init__(self):
           self.consul_client = consul.Consul()
       
       def register_service(self, service_name, service_id, address, port):
           self.consul_client.agent.service.register(
               name=service_name,
               service_id=service_id,
               address=address,
               port=port,
               check=consul.Check.http(f"http://{address}:{port}/health", "10s")
           )
       
       def discover_service(self, service_name):
           _, services = self.consul_client.health.service(service_name, passing=True)
           return [service['Service']['Address'] for service in services]
   
   # Distributed Cache with Redis Cluster
   import redis
   
   class DistributedCache:
       def __init__(self, nodes):
           self.redis_cluster = redis.RedisCluster(
               startup_nodes=nodes,
               decode_responses=True
           )
       
       def get(self, key):
           return self.redis_cluster.get(key)
       
       def set(self, key, value, ttl=None):
           if ttl:
               return self.redis_cluster.setex(key, ttl, value)
           return self.redis_cluster.set(key, value)
       
       def delete(self, key):
           return self.redis_cluster.delete(key)
   ```

3. **Message Queue Implementation**:
   ```python
   # RabbitMQ Message Queue
   import pika
   import json
   
   class MessageQueue:
       def __init__(self, host='localhost'):
           self.connection = pika.BlockingConnection(
               pika.ConnectionParameters(host=host)
           )
           self.channel = self.connection.channel()
       
       def publish_message(self, exchange, routing_key, message):
           self.channel.basic_publish(
               exchange=exchange,
               routing_key=routing_key,
               body=json.dumps(message)
           )
       
       def consume_messages(self, queue_name, callback):
           self.channel.basic_consume(
               queue=queue_name,
               on_message_callback=callback,
               auto_ack=True
           )
           self.channel.start_consuming()
   
   # Kafka Implementation
   from kafka import KafkaProducer, KafkaConsumer
   import json
   
   class KafkaMessageQueue:
       def __init__(self, bootstrap_servers):
           self.producer = KafkaProducer(
               bootstrap_servers=bootstrap_servers,
               value_serializer=lambda v: json.dumps(v).encode('utf-8')
           )
           self.consumer = KafkaConsumer(
               bootstrap_servers=bootstrap_servers,
               value_deserializer=lambda m: json.loads(m.decode('utf-8'))
           )
       
       def publish_message(self, topic, message):
           self.producer.send(topic, message)
           self.producer.flush()
       
       def consume_messages(self, topic, callback):
           self.consumer.subscribe([topic])
           for message in self.consumer:
               callback(message.value)
   ```

### **Q4: Design a cloud-native architecture**
**Answer**:
1. **Cloud-Native Principles**:
   - **Containerization**: Docker containers for consistent deployment
   - **Orchestration**: Kubernetes for container management
   - **Serverless**: AWS Lambda, Azure Functions for event-driven compute
   - **Microservices**: Loosely coupled, independently deployable services
   - **DevOps**: CI/CD pipelines for automated deployment
   - **Observability**: Comprehensive monitoring and logging

2. **Kubernetes Deployment**:
   ```yaml
   # Kubernetes deployment for microservices
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: user-service
   spec:
     replicas: 3
     selector:
       matchLabels:
         app: user-service
     template:
       metadata:
         labels:
           app: user-service
       spec:
         containers:
         - name: user-service
           image: user-service:latest
           ports:
           - containerPort: 8080
           env:
           - name: DB_HOST
             valueFrom:
               configMapKeyRef:
                 name: user-service-config
                 key: db_host
           - name: REDIS_HOST
             valueFrom:
               configMapKeyRef:
                 name: user-service-config
                 key: redis_host
           resources:
             requests:
               memory: "256Mi"
               cpu: "250m"
             limits:
               memory: "512Mi"
               cpu: "500m"
           livenessProbe:
             httpGet:
               path: /health
               port: 8080
             initialDelaySeconds: 30
             periodSeconds: 10
           readinessProbe:
             httpGet:
               path: /ready
               port: 8080
             initialDelaySeconds: 5
             periodSeconds: 5
   ```

3. **Serverless Architecture**:
   ```python
   # AWS Lambda function for user registration
   import json
   import boto3
   from botocore.exceptions import ClientError
   
   def lambda_handler(event, context):
       try:
           # Parse request
           body = json.loads(event['body'])
           email = body['email']
           password = body['password']
           
           # Validate input
           if not email or not password:
               return {
                   'statusCode': 400,
                   'body': json.dumps({'error': 'Email and password are required'})
               }
           
           # Create user in DynamoDB
           dynamodb = boto3.resource('dynamodb')
           table = dynamodb.Table('users')
           
           user = {
               'email': email,
               'password_hash': hash_password(password),
               'created_at': int(time.time())
           }
           
           table.put_item(Item=user)
           
           # Send welcome email via SNS
           sns = boto3.client('sns')
           sns.publish(
               TopicArn='arn:aws:sns:us-east-1:123456789012:welcome-emails',
               Message=json.dumps({
                   'email': email,
                   'type': 'welcome'
               })
           )
           
           return {
               'statusCode': 201,
               'body': json.dumps({'message': 'User created successfully'})
           }
           
       except ClientError as e:
           return {
               'statusCode': 500,
               'body': json.dumps({'error': str(e)})
           }
   ```

### **Q5: Design an event-driven architecture**
**Answer**:
1. **Event-Driven Architecture Components**:
   - **Event Producers**: Services that generate events
   - **Event Consumers**: Services that process events
   - **Event Bus**: Central event routing system
   - **Event Store**: Persistent event storage
   - **Event Sourcing**: Reconstruct state from events
   - **CQRS**: Separate read and write models

2. **Event Sourcing Implementation**:
   ```python
   # Event sourcing for order management
   class OrderEventStore:
       def __init__(self, event_store):
           self.event_store = event_store
       
       def create_order(self, order_data):
           event = {
               'type': 'OrderCreated',
               'data': order_data,
               'timestamp': time.time(),
               'version': 1
           }
           self.event_store.append(event)
           return event['id']
       
       def add_item_to_order(self, order_id, item_data):
           event = {
               'type': 'ItemAddedToOrder',
               'data': {
                   'order_id': order_id,
                   'item': item_data
               },
               'timestamp': time.time(),
               'version': 2
           }
           self.event_store.append(event)
       
       def get_order_state(self, order_id):
           events = self.event_store.get_events(order_id)
           order_state = OrderState()
           
           for event in events:
               order_state.apply(event)
           
           return order_state
   
   class OrderState:
       def __init__(self):
           self.items = []
           self.total_amount = 0
           self.status = 'pending'
       
       def apply(self, event):
           if event['type'] == 'OrderCreated':
               self.status = 'created'
           elif event['type'] == 'ItemAddedToOrder':
               self.items.append(event['data']['item'])
               self.total_amount += event['data']['item']['price']
           elif event['type'] == 'OrderConfirmed':
               self.status = 'confirmed'
   ```

3. **CQRS Implementation**:
   ```python
   # Command side (write model)
   class OrderCommandHandler:
       def __init__(self, event_store):
           self.event_store = event_store
       
       def create_order(self, command):
           event = OrderCreatedEvent(
               order_id=command.order_id,
               user_id=command.user_id,
               items=command.items
           )
           self.event_store.save(event)
       
       def add_item_to_order(self, command):
           event = ItemAddedToOrderEvent(
               order_id=command.order_id,
               item=command.item
           )
           self.event_store.save(event)
   
   # Query side (read model)
   class OrderQueryHandler:
       def __init__(self, read_database):
           self.read_db = read_database
       
       def get_order(self, order_id):
           return self.read_db.get_order(order_id)
       
       def get_user_orders(self, user_id):
           return self.read_db.get_orders_by_user(user_id)
       
       def get_order_summary(self, order_id):
           return self.read_db.get_order_summary(order_id)
   ```

### **Q6: Design a scalable API architecture**
**Answer**:
1. **API Architecture Components**:
   - **API Gateway**: Single entry point with routing, authentication, rate limiting
   - **Load Balancer**: Distribute traffic across API instances
   - **Caching Layer**: Redis for response caching
   - **Rate Limiting**: Prevent API abuse
   - **Authentication**: JWT tokens, OAuth2
   - **Monitoring**: API metrics and analytics

2. **API Gateway Implementation**:
   ```python
   # FastAPI API Gateway
   from fastapi import FastAPI, HTTPException, Depends
   from fastapi.security import HTTPBearer
   import httpx
   import redis
   import json
   
   app = FastAPI()
   security = HTTPBearer()
   redis_client = redis.Redis(host='localhost', port=6379, db=0)
   
   class APIGateway:
       def __init__(self):
           self.services = {
               'users': 'http://user-service:8080',
               'products': 'http://product-service:8080',
               'orders': 'http://order-service:8080'
           }
           self.http_client = httpx.AsyncClient()
       
       async def route_request(self, service_name, path, method, data=None, headers=None):
           # Check cache first
           cache_key = f"{service_name}:{path}:{method}"
           cached_response = redis_client.get(cache_key)
           
           if cached_response and method == 'GET':
               return json.loads(cached_response)
           
           # Forward request to service
           service_url = self.services.get(service_name)
           if not service_url:
               raise HTTPException(status_code=404, detail="Service not found")
           
           url = f"{service_url}{path}"
           
           if method == 'GET':
               response = await self.http_client.get(url, headers=headers)
           elif method == 'POST':
               response = await self.http_client.post(url, json=data, headers=headers)
           elif method == 'PUT':
               response = await self.http_client.put(url, json=data, headers=headers)
           elif method == 'DELETE':
               response = await self.http_client.delete(url, headers=headers)
           
           # Cache successful GET responses
           if method == 'GET' and response.status_code == 200:
               redis_client.setex(cache_key, 300, response.text)  # 5 minutes TTL
           
           return response.json()
   
   gateway = APIGateway()
   
   @app.get("/api/users/{user_id}")
   async def get_user(user_id: str, token: str = Depends(security)):
       return await gateway.route_request('users', f'/users/{user_id}', 'GET')
   
   @app.post("/api/orders")
   async def create_order(order_data: dict, token: str = Depends(security)):
       return await gateway.route_request('orders', '/orders', 'POST', order_data)
   ```

3. **Rate Limiting Implementation**:
   ```python
   # Rate limiting with Redis
   import time
   from collections import defaultdict
   
   class RateLimiter:
       def __init__(self, redis_client):
           self.redis_client = redis_client
       
       def is_allowed(self, client_id, limit=100, window=60):
           key = f"rate_limit:{client_id}"
           current = time.time()
           
           # Remove old entries
           self.redis_client.zremrangebyscore(key, 0, current - window)
           
           # Count current requests
           count = self.redis_client.zcard(key)
           
           if count < limit:
               # Add current request
               self.redis_client.zadd(key, {str(current): current})
               self.redis_client.expire(key, window)
               return True
           
           return False
   
   # Usage in API Gateway
   rate_limiter = RateLimiter(redis_client)
   
   def check_rate_limit(client_id):
       if not rate_limiter.is_allowed(client_id):
           raise HTTPException(status_code=429, detail="Rate limit exceeded")
   ```

### **Q7: Design a high-availability system architecture**
**Answer**:
1. **High Availability Components**:
   - **Load Balancer**: Distribute traffic across healthy instances
   - **Auto-scaling**: Automatically scale based on demand
   - **Health Checks**: Monitor service health
   - **Failover**: Automatic switching to backup systems
   - **Data Replication**: Multiple copies of data
   - **Monitoring**: Comprehensive system monitoring

2. **Load Balancer Configuration**:
   ```nginx
   # Nginx load balancer configuration
   upstream backend {
       least_conn;  # Least connections algorithm
       server app1.example.com:8080 max_fails=3 fail_timeout=30s;
       server app2.example.com:8080 max_fails=3 fail_timeout=30s;
       server app3.example.com:8080 max_fails=3 fail_timeout=30s;
       keepalive 32;
   }
   
   server {
       listen 80;
       server_name api.example.com;
       
       location / {
           proxy_pass http://backend;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
           
           # Health check
           proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
       }
       
       # Health check endpoint
       location /health {
           access_log off;
           return 200 "healthy\n";
           add_header Content-Type text/plain;
       }
   }
   ```

3. **Auto-scaling Implementation**:
   ```python
   # Kubernetes HPA (Horizontal Pod Autoscaler)
   apiVersion: autoscaling/v2
   kind: HorizontalPodAutoscaler
   metadata:
     name: api-service-hpa
   spec:
     scaleTargetRef:
       apiVersion: apps/v1
       kind: Deployment
       name: api-service
     minReplicas: 2
     maxReplicas: 10
     metrics:
     - type: Resource
       resource:
         name: cpu
         target:
           type: Utilization
           averageUtilization: 70
     - type: Resource
       resource:
         name: memory
         target:
           type: Utilization
           averageUtilization: 80
     behavior:
       scaleUp:
         stabilizationWindowSeconds: 60
         policies:
         - type: Percent
           value: 100
           periodSeconds: 15
       scaleDown:
         stabilizationWindowSeconds: 300
         policies:
         - type: Percent
           value: 10
           periodSeconds: 60
   ```

### **Q8: Design a security-focused architecture**
**Answer**:
1. **Security Architecture Components**:
   - **Identity and Access Management (IAM)**: User authentication and authorization
   - **API Security**: Rate limiting, input validation, encryption
   - **Network Security**: VPC, firewalls, private subnets
   - **Data Security**: Encryption at rest and in transit
   - **Monitoring**: Security event monitoring and alerting
   - **Compliance**: GDPR, HIPAA, SOC2 compliance

2. **Security Implementation**:
   ```python
   # JWT Authentication Middleware
   import jwt
   from functools import wraps
   from flask import request, jsonify
   
   class SecurityManager:
       def __init__(self, secret_key):
           self.secret_key = secret_key
       
       def generate_token(self, user_id, permissions):
           payload = {
               'user_id': user_id,
               'permissions': permissions,
               'exp': time.time() + 3600  # 1 hour expiration
           }
           return jwt.encode(payload, self.secret_key, algorithm='HS256')
       
       def verify_token(self, token):
           try:
               payload = jwt.decode(token, self.secret_key, algorithms=['HS256'])
               return payload
           except jwt.ExpiredSignatureError:
               raise Exception("Token expired")
           except jwt.InvalidTokenError:
               raise Exception("Invalid token")
   
   def require_auth(f):
       @wraps(f)
       def decorated_function(*args, **kwargs):
           token = request.headers.get('Authorization')
           if not token:
               return jsonify({'error': 'No token provided'}), 401
           
           try:
               token = token.split(' ')[1]  # Remove 'Bearer ' prefix
               payload = security_manager.verify_token(token)
               request.user = payload
               return f(*args, **kwargs)
           except Exception as e:
               return jsonify({'error': str(e)}), 401
       
       return decorated_function
   ```

3. **Data Encryption**:
   ```python
   # Data encryption utilities
   from cryptography.fernet import Fernet
   from cryptography.hazmat.primitives import hashes
   from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
   import base64
   
   class DataEncryption:
       def __init__(self, password):
           salt = b'salt_123'  # In production, use random salt
           kdf = PBKDF2HMAC(
               algorithm=hashes.SHA256(),
               length=32,
               salt=salt,
               iterations=100000,
           )
           key = base64.urlsafe_b64encode(kdf.derive(password.encode()))
           self.cipher_suite = Fernet(key)
       
       def encrypt_data(self, data):
           if isinstance(data, str):
               return self.cipher_suite.encrypt(data.encode()).decode()
           return data
       
       def decrypt_data(self, encrypted_data):
           if isinstance(encrypted_data, str):
               try:
                   return self.cipher_suite.decrypt(encrypted_data.encode()).decode()
               except:
                   return encrypted_data
           return encrypted_data
   ```

---

## 🎯 Interview Preparation Tips

### **Key Areas to Focus On:**

1. **Architecture Patterns**:
   - Microservices vs. Monoliths
   - Event-driven architecture
   - CQRS and Event Sourcing
   - API Gateway patterns

2. **Scalability and Performance**:
   - Horizontal vs. vertical scaling
   - Load balancing strategies
   - Caching strategies
   - Database sharding

3. **Reliability and Availability**:
   - High availability patterns
   - Disaster recovery
   - Fault tolerance
   - Circuit breaker patterns

4. **Security and Compliance**:
   - Authentication and authorization
   - Data encryption
   - Network security
   - Compliance requirements

5. **Cloud and DevOps**:
   - Container orchestration
   - Serverless architecture
   - CI/CD pipelines
   - Infrastructure as Code

### **Practice Questions to Prepare For:**

- Design a scalable microservices architecture
- Migrate from monolith to microservices
- Implement event-driven communication
- Design high-availability systems
- Implement security measures
- Optimize for performance and scalability
- Design cloud-native applications
- Handle distributed system challenges

**Remember**: Focus on practical implementation, understand trade-offs, and be ready to discuss real-world scenarios and challenges.
