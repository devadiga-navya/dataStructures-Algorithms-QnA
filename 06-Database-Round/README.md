# Database Interview Preparation Guide

## 🎯 Overview
Comprehensive interview preparation for database-related roles covering SQL, NoSQL, MongoDB, Vector Databases, GraphQL, and modern database technologies.

## 📊 SQL Database Questions

### **Q1: Design and optimize a relational database schema**
**Answer**:
1. **Database Design Principles**:
   - **Normalization**: Eliminate redundancy and anomalies
   - **Denormalization**: Optimize for read performance
   - **Indexing Strategy**: Balance query performance and storage
   - **Constraints**: Ensure data integrity

2. **E-commerce Database Schema**:
   ```sql
   CREATE TABLE users (
       user_id INT PRIMARY KEY AUTO_INCREMENT,
       email VARCHAR(255) UNIQUE NOT NULL,
       password_hash VARCHAR(255) NOT NULL,
       first_name VARCHAR(100),
       last_name VARCHAR(100),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       INDEX idx_email (email)
   );

   CREATE TABLE products (
       product_id INT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(255) NOT NULL,
       description TEXT,
       price DECIMAL(10,2) NOT NULL,
       category_id INT,
       stock_quantity INT DEFAULT 0,
       FOREIGN KEY (category_id) REFERENCES categories(category_id),
       INDEX idx_category (category_id),
       FULLTEXT INDEX idx_search (name, description)
   );
   ```

### **Q2: Implement complex SQL queries and stored procedures**
**Answer**:
1. **Advanced SQL Queries**:
   ```sql
   -- Window functions for ranking
   SELECT 
       product_id, name, price,
       ROW_NUMBER() OVER (ORDER BY price DESC) as price_rank,
       LAG(price, 1) OVER (ORDER BY price DESC) as previous_price
   FROM products
   WHERE category_id = 1;

   -- Recursive CTE for hierarchical data
   WITH RECURSIVE category_tree AS (
       SELECT category_id, name, parent_id, 0 as level
       FROM categories WHERE parent_id IS NULL
       UNION ALL
       SELECT c.category_id, c.name, c.parent_id, ct.level + 1
       FROM categories c
       JOIN category_tree ct ON c.parent_id = ct.category_id
   )
   SELECT * FROM category_tree ORDER BY level, name;
   ```

2. **Stored Procedures**:
   ```sql
   DELIMITER //
   CREATE PROCEDURE GetUserOrders(
       IN p_user_id INT,
       IN p_status VARCHAR(20),
       OUT p_total_orders INT
   )
   BEGIN
       SELECT COUNT(*) INTO p_total_orders
       FROM orders
       WHERE user_id = p_user_id
       AND (p_status IS NULL OR status = p_status);
   END //
   DELIMITER ;
   ```

## 🗄️ NoSQL Database Questions

### **Q3: Design a document-based NoSQL database schema**
**Answer**:
1. **MongoDB Document Design**:
   ```javascript
   // User document with embedded data
   {
     "_id": ObjectId("507f1f77bcf86cd799439011"),
     "email": "user@example.com",
     "profile": {
       "firstName": "John",
       "lastName": "Doe",
       "address": {
         "street": "123 Main St",
         "city": "New York",
         "state": "NY"
       }
     },
     "preferences": {
       "language": "en",
       "timezone": "America/New_York",
       "notifications": {
         "email": true,
         "sms": false,
         "push": true
       }
     }
   }

   // Product document with arrays
   {
     "_id": ObjectId("507f1f77bcf86cd799439012"),
     "name": "Smartphone X",
     "price": 999.99,
     "category": "Electronics",
     "tags": ["smartphone", "mobile", "tech"],
     "specifications": {
       "screen": "6.1 inch OLED",
       "storage": "128GB"
     },
     "reviews": [
       {
         "userId": ObjectId("507f1f77bcf86cd799439011"),
         "rating": 5,
         "comment": "Great phone!"
       }
     ]
   }
   ```

2. **MongoDB Aggregation Pipeline**:
   ```javascript
   db.orders.aggregate([
     {
       $match: {
         createdAt: {
           $gte: new Date("2023-01-01"),
           $lt: new Date("2024-01-01")
         }
       }
     },
     {
       $lookup: {
         from: "users",
         localField: "userId",
         foreignField: "_id",
         as: "user"
       }
     },
     {
       $group: {
         _id: {
           year: { $year: "$createdAt" },
           month: { $month: "$createdAt" }
         },
         totalSales: { $sum: "$totalAmount" },
         orderCount: { $sum: 1 }
       }
     }
   ]);
   ```

### **Q4: Implement Redis caching strategies**
**Answer**:
1. **Redis Data Structures and Caching**:
   ```python
   import redis
   import json
   
   class RedisCache:
       def __init__(self):
           self.redis_client = redis.Redis(host='localhost', port=6379, db=0)
       
       def cache_user_profile(self, user_id, user_data, ttl=3600):
           key = f"user:{user_id}:profile"
           self.redis_client.setex(key, ttl, json.dumps(user_data))
       
       def get_user_profile(self, user_id):
           key = f"user:{user_id}:profile"
           data = self.redis_client.get(key)
           return json.loads(data) if data else None
       
       def increment_product_views(self, product_id):
           key = f"product:{product_id}:views"
           return self.redis_client.incr(key)
       
       def get_top_products(self, limit=10):
           return self.redis_client.zrevrange("product:views", 0, limit-1, withscores=True)
   ```

## 🔍 Vector Database Questions

### **Q5: Design a vector database for similarity search**
**Answer**:
1. **Vector Database Architecture**:
   ```python
   import numpy as np
   from sentence_transformers import SentenceTransformer
   import faiss
   
   class VectorDatabase:
       def __init__(self, dimension=768):
           self.dimension = dimension
           self.encoder = SentenceTransformer('all-MiniLM-L6-v2')
           self.index = faiss.IndexFlatIP(dimension)
           self.documents = []
           self.metadata = []
       
       def add_documents(self, texts, metadata_list):
           embeddings = self.encoder.encode(texts)
           faiss.normalize_L2(embeddings)
           self.index.add(embeddings.astype('float32'))
           self.documents.extend(texts)
           self.metadata.extend(metadata_list)
       
       def search(self, query, k=5):
           query_vector = self.encoder.encode([query])
           faiss.normalize_L2(query_vector)
           scores, indices = self.index.search(query_vector.astype('float32'), k)
           
           results = []
           for score, idx in zip(scores[0], indices[0]):
               results.append({
                   'document': self.documents[idx],
                   'metadata': self.metadata[idx],
                   'similarity_score': float(score)
               })
           return results
   ```

2. **Pinecone Vector Database Implementation**:
   ```python
   import pinecone
   from sentence_transformers import SentenceTransformer
   
   class PineconeVectorDB:
       def __init__(self, api_key, environment, index_name):
           pinecone.init(api_key=api_key, environment=environment)
           self.encoder = SentenceTransformer('all-MiniLM-L6-v2')
           
           if index_name not in pinecone.list_indexes():
               pinecone.create_index(
                   name=index_name,
                   dimension=768,
                   metric='cosine'
               )
           
           self.index = pinecone.Index(index_name)
       
       def upsert_documents(self, documents):
           vectors = []
           for doc_id, text, metadata in documents:
               embedding = self.encoder.encode(text).tolist()
               vectors.append({
                   'id': doc_id,
                   'values': embedding,
                   'metadata': metadata
               })
           self.index.upsert(vectors=vectors)
   ```

## 🎯 GraphQL Questions

### **Q6: Design a GraphQL API with resolvers**
**Answer**:
1. **GraphQL Schema Design**:
   ```graphql
   type User {
     id: ID!
     email: String!
     firstName: String
     lastName: String
     profile: UserProfile
     orders: [Order!]!
     createdAt: DateTime!
   }
   
   type Product {
     id: ID!
     name: String!
     description: String
     price: Float!
     category: Category!
     reviews: [Review!]!
     stockQuantity: Int!
   }
   
   type Order {
     id: ID!
     user: User!
     items: [OrderItem!]!
     totalAmount: Float!
     status: OrderStatus!
     createdAt: DateTime!
   }
   
   enum OrderStatus {
     PENDING
     CONFIRMED
     SHIPPED
     DELIVERED
   }
   
   type Query {
     user(id: ID!): User
     users(limit: Int = 10, offset: Int = 0): [User!]!
     product(id: ID!): Product
     products(categoryId: ID, search: String): [Product!]!
   }
   
   type Mutation {
     createUser(input: CreateUserInput!): User!
     updateUser(id: ID!, input: UpdateUserInput!): User!
     createOrder(input: CreateOrderInput!): Order!
   }
   ```

2. **GraphQL Resolvers Implementation**:
   ```javascript
   const resolvers = {
     Query: {
       user: async (parent, { id }, { dataSources }) => {
         return await dataSources.userAPI.getUser(id);
       },
       
       products: async (parent, { categoryId, search }, { dataSources }) => {
         return await dataSources.productAPI.getProducts({
           categoryId,
           search
         });
       }
     },
     
     User: {
       orders: async (parent, args, { dataSources }) => {
         return await dataSources.orderAPI.getOrdersByUser(parent.id);
       }
     },
     
     Product: {
       category: async (parent, args, { dataSources }) => {
         return await dataSources.categoryAPI.getCategory(parent.categoryId);
       },
       
       reviews: async (parent, args, { dataSources }) => {
         return await dataSources.reviewAPI.getProductReviews(parent.id);
       }
     },
     
     Mutation: {
       createUser: async (parent, { input }, { dataSources }) => {
         return await dataSources.userAPI.createUser(input);
       },
       
       createOrder: async (parent, { input }, { dataSources }) => {
         return await dataSources.orderAPI.createOrder(input);
       }
     }
   };
   ```

### **Q7: Implement GraphQL with data loaders for N+1 problem**
**Answer**:
1. **Data Loader Implementation**:
   ```javascript
   const DataLoader = require('dataloader');
   
   class UserDataLoader {
     constructor(userAPI) {
       this.userAPI = userAPI;
       this.userLoader = new DataLoader(async (userIds) => {
         const users = await this.userAPI.getUsersByIds(userIds);
         return userIds.map(id => users.find(user => user.id === id) || null);
       });
     }
   
     async getUser(id) {
       return this.userLoader.load(id);
     }
   }
   
   class ProductDataLoader {
     constructor(productAPI) {
       this.productAPI = productAPI;
       this.productLoader = new DataLoader(async (productIds) => {
         const products = await this.productAPI.getProductsByIds(productIds);
         return productIds.map(id => products.find(product => product.id === id) || null);
       });
     }
   
     async getProduct(id) {
       return this.productLoader.load(id);
     }
   }
   ```

## 🔄 Database Migration and Versioning

### **Q8: Design database migration strategies**
**Answer**:
1. **Migration Framework**:
   ```python
   from alembic import op
   import sqlalchemy as sa
   
   def upgrade():
       # Create user_preferences table
       op.create_table(
           'user_preferences',
           sa.Column('id', sa.Integer(), nullable=False),
           sa.Column('user_id', sa.Integer(), nullable=False),
           sa.Column('language', sa.String(10), nullable=False, default='en'),
           sa.Column('timezone', sa.String(50), nullable=False, default='UTC'),
           sa.Column('notifications_email', sa.Boolean(), nullable=False, default=True),
           sa.ForeignKeyConstraint(['user_id'], ['users.id'], ondelete='CASCADE'),
           sa.PrimaryKeyConstraint('id')
       )
       
       op.create_index('ix_user_preferences_user_id', 'user_preferences', ['user_id'])
       op.create_unique_constraint('uq_user_preferences_user_id', 'user_preferences', ['user_id'])
   
   def downgrade():
       op.drop_constraint('uq_user_preferences_user_id', 'user_preferences', type_='unique')
       op.drop_index('ix_user_preferences_user_id', table_name='user_preferences')
       op.drop_table('user_preferences')
   ```

2. **Data Migration Script**:
   ```python
   import pandas as pd
   from sqlalchemy import create_engine
   
   class DataMigration:
       def __init__(self, source_db_url, target_db_url):
           self.source_engine = create_engine(source_db_url)
           self.target_engine = create_engine(target_db_url)
       
       def migrate_user_data(self):
           query = """
           SELECT id, email, first_name, last_name, created_at
           FROM users
           """
           
           df = pd.read_sql(query, self.source_engine)
           df['email'] = df['email'].str.lower()
           df['first_name'] = df['first_name'].str.title()
           
           df.to_sql('users', self.target_engine, if_exists='append', index=False)
   ```

## 🔐 Database Security and Access Control

### **Q9: Implement database security and access control**
**Answer**:
1. **Row-Level Security (RLS)**:
   ```sql
   -- Enable RLS on users table
   ALTER TABLE users ENABLE ROW LEVEL SECURITY;
   
   -- Create policy for users to see only their own data
   CREATE POLICY user_own_data ON users
   FOR ALL
   USING (id = current_user_id());
   
   -- Create policy for admins to see all data
   CREATE POLICY admin_all_data ON users
   FOR ALL
   USING (current_user_role() = 'admin');
   ```

2. **Database Encryption**:
   ```python
   from cryptography.fernet import Fernet
   
   class DatabaseEncryption:
       def __init__(self):
           self.key = Fernet.generate_key()
           self.cipher_suite = Fernet(self.key)
       
       def encrypt_sensitive_data(self, data):
           if isinstance(data, str):
               return self.cipher_suite.encrypt(data.encode()).decode()
           return data
       
       def decrypt_sensitive_data(self, encrypted_data):
           if isinstance(encrypted_data, str):
               try:
                   return self.cipher_suite.decrypt(encrypted_data.encode()).decode()
               except:
                   return encrypted_data
           return encrypted_data
   ```

3. **Audit Logging**:
   ```sql
   CREATE TABLE audit_logs (
       id BIGSERIAL PRIMARY KEY,
       table_name VARCHAR(100) NOT NULL,
       operation VARCHAR(20) NOT NULL,
       record_id VARCHAR(100),
       old_values JSONB,
       new_values JSONB,
       user_id INTEGER,
       timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   
   CREATE OR REPLACE FUNCTION audit_trigger_function()
   RETURNS TRIGGER AS $$
   BEGIN
       IF TG_OP = 'INSERT' THEN
           INSERT INTO audit_logs (table_name, operation, record_id, new_values, user_id)
           VALUES (TG_TABLE_NAME, 'INSERT', NEW.id, to_jsonb(NEW), current_user_id());
           RETURN NEW;
       ELSIF TG_OP = 'UPDATE' THEN
           INSERT INTO audit_logs (table_name, operation, record_id, old_values, new_values, user_id)
           VALUES (TG_TABLE_NAME, 'UPDATE', NEW.id, to_jsonb(OLD), to_jsonb(NEW), current_user_id());
           RETURN NEW;
       END IF;
       RETURN NULL;
   END;
   $$ LANGUAGE plpgsql;
   ```

## 🎯 Interview Preparation Tips

### **Key Areas to Focus On:**

1. **Database Design Principles**:
   - Normalization vs. denormalization
   - Indexing strategies
   - Query optimization
   - ACID properties

2. **NoSQL vs. SQL**:
   - When to use each type
   - CAP theorem understanding
   - Consistency models
   - Scalability considerations

3. **Modern Database Technologies**:
   - Vector databases for AI/ML
   - GraphQL for API design
   - Real-time databases
   - Distributed databases

4. **Performance and Scalability**:
   - Caching strategies
   - Sharding techniques
   - Read replicas
   - Connection pooling

5. **Security and Compliance**:
   - Data encryption
   - Access control
   - Audit logging
   - GDPR compliance

### **Practice Questions to Prepare For:**

- Design a database schema for a specific domain
- Optimize slow queries
- Implement caching strategies
- Design for high availability
- Handle data migration scenarios
- Implement security measures
- Design GraphQL schemas
- Work with vector databases for ML applications

**Remember**: Focus on practical implementation, understand trade-offs, and be ready to discuss real-world scenarios and challenges.

## 🍃 MongoDB Specific Questions

### **Q10: Explain MongoDB architecture and components**
**Answer**:
1. **MongoDB Architecture Components**:
   - **Mongod**: Primary database server process
   - **Mongos**: Query router for sharded clusters
   - **Config Servers**: Store metadata for sharded clusters
   - **Replica Set**: Multiple mongod instances for high availability
   - **Shard**: Horizontal partition of data

2. **Replica Set Architecture**:
   ```javascript
   // Replica set configuration
   {
     "_id": "myReplicaSet",
     "members": [
       {
         "_id": 0,
         "host": "mongodb0.example.net:27017",
         "priority": 2
       },
       {
         "_id": 1,
         "host": "mongodb1.example.net:27017",
         "priority": 1
       },
       {
         "_id": 2,
         "host": "mongodb2.example.net:27017",
         "priority": 1
       }
     ]
   }

   // Initialize replica set
   rs.initiate({
     _id: "myReplicaSet",
     members: [
       { _id: 0, host: "mongodb0.example.net:27017" },
       { _id: 1, host: "mongodb1.example.net:27017" },
       { _id: 2, host: "mongodb2.example.net:27017" }
     ]
   });
   ```

3. **Sharded Cluster Architecture**:
   ```javascript
   // Shard configuration
   sh.addShard("shard0/mongodb0.example.net:27018")
   sh.addShard("shard1/mongodb1.example.net:27018")
   sh.addShard("shard2/mongodb2.example.net:27018")

   // Enable sharding for database
   sh.enableSharding("ecommerce")

   // Shard collection with hashed shard key
   sh.shardCollection("ecommerce.products", { "productId": "hashed" })
   ```

### **Q11: Design MongoDB schema for different use cases**
**Answer**:
1. **E-commerce Product Schema**:
   ```javascript
   // Products collection with embedded variants
   {
     "_id": ObjectId("507f1f77bcf86cd799439011"),
     "name": "iPhone 15 Pro",
     "brand": "Apple",
     "category": "Electronics",
     "description": "Latest iPhone with advanced features",
     "price": {
       "base": 999.99,
       "currency": "USD",
       "discount": 0.1
     },
     "specifications": {
       "screen": "6.1 inch OLED",
       "storage": ["128GB", "256GB", "512GB"],
       "colors": ["Natural Titanium", "Blue Titanium", "White Titanium"],
       "camera": "48MP main camera"
     },
     "inventory": {
       "total": 150,
       "reserved": 25,
       "available": 125
     },
     "ratings": {
       "average": 4.5,
       "count": 1250,
       "distribution": {
         "5": 800,
         "4": 300,
         "3": 100,
         "2": 30,
         "1": 20
       }
     },
     "tags": ["smartphone", "mobile", "tech", "apple"],
     "createdAt": ISODate("2023-09-15T00:00:00Z"),
     "updatedAt": ISODate("2023-12-01T00:00:00Z")
   }

   // User profiles with embedded addresses
   {
     "_id": ObjectId("507f1f77bcf86cd799439012"),
     "email": "user@example.com",
     "profile": {
       "firstName": "John",
       "lastName": "Doe",
       "phone": "+1-555-0123",
       "dateOfBirth": ISODate("1990-05-15T00:00:00Z"),
       "avatar": "https://example.com/avatars/john.jpg"
     },
     "addresses": [
       {
         "type": "billing",
         "street": "123 Main St",
         "city": "New York",
         "state": "NY",
         "zipCode": "10001",
         "country": "USA",
         "isDefault": true
       },
       {
         "type": "shipping",
         "street": "456 Oak Ave",
         "city": "Los Angeles",
         "state": "CA",
         "zipCode": "90210",
         "country": "USA",
         "isDefault": false
       }
     ],
     "preferences": {
       "language": "en",
       "timezone": "America/New_York",
       "currency": "USD",
       "notifications": {
         "email": true,
         "sms": false,
         "push": true,
         "marketing": false
       }
     },
     "membership": {
       "tier": "gold",
       "points": 2500,
       "joinDate": ISODate("2022-01-15T00:00:00Z")
     }
   }
   ```

2. **Social Media Post Schema**:
   ```javascript
   // Posts with embedded comments and reactions
   {
     "_id": ObjectId("507f1f77bcf86cd799439013"),
     "authorId": ObjectId("507f1f77bcf86cd799439012"),
     "content": {
       "text": "Just got my new iPhone 15 Pro! Amazing camera quality! 📱📸",
       "media": [
         {
           "type": "image",
           "url": "https://example.com/images/iphone15.jpg",
           "thumbnail": "https://example.com/thumbnails/iphone15.jpg",
           "alt": "iPhone 15 Pro camera"
         }
       ]
     },
     "metadata": {
       "language": "en",
       "hashtags": ["#iPhone15", "#Apple", "#Photography"],
       "mentions": ["@apple", "@photography"],
       "location": {
         "type": "Point",
         "coordinates": [-74.006, 40.7128],
         "name": "New York, NY"
       }
     },
     "engagement": {
       "likes": 1250,
       "shares": 89,
       "comments": 156,
       "views": 15420
     },
     "comments": [
       {
         "_id": ObjectId("507f1f77bcf86cd799439014"),
         "authorId": ObjectId("507f1f77bcf86cd799439015"),
         "text": "Looks amazing! How's the battery life?",
         "createdAt": ISODate("2023-12-01T10:30:00Z"),
         "likes": 45,
         "replies": [
           {
             "authorId": ObjectId("507f1f77bcf86cd799439012"),
             "text": "Battery life is incredible! Lasts all day with heavy use.",
             "createdAt": ISODate("2023-12-01T11:15:00Z"),
             "likes": 12
           }
         ]
       }
     ],
     "createdAt": ISODate("2023-12-01T09:00:00Z"),
     "updatedAt": ISODate("2023-12-01T11:15:00Z")
   }
   ```

### **Q12: Implement complex MongoDB aggregation pipelines**
**Answer**:
1. **E-commerce Analytics Pipeline**:
   ```javascript
   // Sales analytics with product performance
   db.orders.aggregate([
     {
       $match: {
         createdAt: {
           $gte: new Date("2023-01-01"),
           $lt: new Date("2024-01-01")
         },
         status: { $in: ["confirmed", "shipped", "delivered"] }
       }
     },
     {
       $unwind: "$items"
     },
     {
       $lookup: {
         from: "products",
         localField: "items.productId",
         foreignField: "_id",
         as: "product"
       }
     },
     {
       $unwind: "$product"
     },
     {
       $group: {
         _id: {
           productId: "$product._id",
           productName: "$product.name",
           category: "$product.category",
           month: { $month: "$createdAt" },
           year: { $year: "$createdAt" }
         },
         totalQuantity: { $sum: "$items.quantity" },
         totalRevenue: { $sum: { $multiply: ["$items.price", "$items.quantity"] } },
         orderCount: { $sum: 1 },
         avgOrderValue: { $avg: { $multiply: ["$items.price", "$items.quantity"] } }
       }
     },
     {
       $group: {
         _id: {
           productId: "$_id.productId",
           productName: "$_id.productName",
           category: "$_id.category"
         },
         totalQuantity: { $sum: "$totalQuantity" },
         totalRevenue: { $sum: "$totalRevenue" },
         totalOrders: { $sum: "$orderCount" },
         avgOrderValue: { $avg: "$avgOrderValue" },
         monthlyData: {
           $push: {
             month: "$_id.month",
             year: "$_id.year",
             quantity: "$totalQuantity",
             revenue: "$totalRevenue",
             orders: "$orderCount"
           }
         }
       }
     },
     {
       $sort: { totalRevenue: -1 }
     },
     {
       $limit: 20
     }
   ]);
   ```

2. **User Behavior Analysis Pipeline**:
   ```javascript
   // User engagement and retention analysis
   db.userSessions.aggregate([
     {
       $match: {
         timestamp: {
           $gte: new Date("2023-11-01"),
           $lt: new Date("2023-12-01")
         }
       }
     },
     {
       $group: {
         _id: {
           userId: "$userId",
           date: { $dateToString: { format: "%Y-%m-%d", date: "$timestamp" } }
         },
         sessionCount: { $sum: 1 },
         totalDuration: { $sum: "$duration" },
         pageViews: { $sum: "$pageViews" },
         actions: { $push: "$actions" }
       }
     },
     {
       $group: {
         _id: "$_id.userId",
         totalSessions: { $sum: "$sessionCount" },
         totalDuration: { $sum: "$totalDuration" },
         totalPageViews: { $sum: "$pageViews" },
         activeDays: { $sum: 1 },
         avgSessionDuration: { $avg: "$totalDuration" },
         avgPageViewsPerSession: { $avg: { $divide: ["$pageViews", "$sessionCount"] } },
         allActions: { $push: "$actions" }
       }
     },
     {
       $lookup: {
         from: "users",
         localField: "_id",
         foreignField: "_id",
         as: "userInfo"
       }
     },
     {
       $unwind: "$userInfo"
     },
     {
       $addFields: {
         userSegment: {
           $cond: {
             if: { $gte: ["$activeDays", 20] },
             then: "highly_active",
             else: {
               $cond: {
                 if: { $gte: ["$activeDays", 10] },
                 then: "active",
                 else: "inactive"
               }
             }
           }
         },
         engagementScore: {
           $add: [
             { $multiply: ["$totalSessions", 0.3] },
             { $multiply: ["$totalPageViews", 0.2] },
             { $multiply: ["$avgSessionDuration", 0.5] }
           ]
         }
       }
     },
     {
       $group: {
         _id: "$userSegment",
         userCount: { $sum: 1 },
         avgEngagementScore: { $avg: "$engagementScore" },
         avgSessions: { $avg: "$totalSessions" },
         avgPageViews: { $avg: "$totalPageViews" }
       }
     },
     {
       $sort: { avgEngagementScore: -1 }
     }
   ]);
   ```

### **Q13: Optimize MongoDB performance with indexing strategies**
**Answer**:
1. **Index Creation and Management**:
   ```javascript
   // Single field indexes
   db.products.createIndex({ "name": 1 });
   db.products.createIndex({ "category": 1 });
   db.products.createIndex({ "price": -1 });

   // Compound indexes for common query patterns
   db.products.createIndex({ "category": 1, "price": -1 });
   db.products.createIndex({ "brand": 1, "category": 1, "price": -1 });

   // Text index for search functionality
   db.products.createIndex({
     "name": "text",
     "description": "text",
     "tags": "text"
   }, {
     "weights": {
       "name": 10,
       "description": 5,
       "tags": 3
     },
     "name": "product_search_index"
   });

   // Geospatial index for location-based queries
   db.stores.createIndex({ "location": "2dsphere" });

   // TTL index for automatic document expiration
   db.sessions.createIndex({ "lastActivity": 1 }, { expireAfterSeconds: 3600 });

   // Partial index for filtered queries
   db.products.createIndex(
     { "category": 1, "price": 1 },
     { partialFilterExpression: { "stockQuantity": { $gt: 0 } } }
   );

   // Sparse index for optional fields
   db.users.createIndex({ "phone": 1 }, { sparse: true });
   ```

2. **Index Analysis and Optimization**:
   ```javascript
   // Analyze query performance
   db.products.find({
     category: "Electronics",
     price: { $gte: 500, $lte: 1000 },
     stockQuantity: { $gt: 0 }
   }).explain("executionStats");

   // Check index usage
   db.products.getIndexes();

   // Monitor index usage
   db.products.aggregate([
     { $indexStats: {} }
   ]);

   // Find unused indexes
   db.products.aggregate([
     { $indexStats: {} },
     { $match: { "accesses.ops": { $lt: 100 } } }
   ]);
   ```

### **Q14: Implement MongoDB sharding and data distribution**
**Answer**:
1. **Sharding Configuration**:
   ```javascript
   // Enable sharding for database
   sh.enableSharding("ecommerce");

   // Shard collections with different strategies
   // Hash-based sharding for even distribution
   sh.shardCollection("ecommerce.products", { "productId": "hashed" });

   // Range-based sharding for date-based queries
   sh.shardCollection("ecommerce.orders", { "createdAt": 1 });

   // Compound shard key for complex queries
   sh.shardCollection("ecommerce.userSessions", { "userId": 1, "timestamp": -1 });

   // Check shard distribution
   db.orders.getShardDistribution();

   // Monitor chunk distribution
   sh.status();
   ```

2. **Sharding Best Practices**:
   ```javascript
   // Pre-split chunks for even distribution
   sh.splitAt("ecommerce.orders", { "createdAt": new Date("2023-06-01") });
   sh.splitAt("ecommerce.orders", { "createdAt": new Date("2023-09-01") });
   sh.splitAt("ecommerce.orders", { "createdAt": new Date("2023-12-01") });

   // Move chunks to balance load
   sh.moveChunk("ecommerce.orders", { "createdAt": new Date("2023-07-15") }, "shard1");

   // Check chunk distribution
   use config;
   db.chunks.find({ "ns": "ecommerce.orders" }).sort({ "min.createdAt": 1 });

   // Monitor shard performance
   db.adminCommand({ "serverStatus": 1 });
   ```

### **Q15: Implement MongoDB change streams for real-time applications**
**Answer**:
1. **Change Streams Implementation**:
   ```javascript
   // Watch for changes in products collection
   const changeStream = db.products.watch([
     {
       $match: {
         "operationType": { $in: ["insert", "update", "delete"] },
         "fullDocument.category": "Electronics"
       }
     }
   ]);

   // Process change events
   changeStream.forEach(change => {
     console.log("Change detected:", change);
     
     switch (change.operationType) {
       case "insert":
         handleProductAdded(change.fullDocument);
         break;
       case "update":
         handleProductUpdated(change.documentKey._id, change.updateDescription);
         break;
       case "delete":
         handleProductDeleted(change.documentKey._id);
         break;
     }
   });
   ```

2. **Real-time Inventory Management**:
   ```javascript
   // Watch inventory changes
   const inventoryStream = db.products.watch([
     {
       $match: {
         "operationType": "update",
         "updateDescription.updatedFields.stockQuantity": { $exists: true }
       }
     }
   ]);

   inventoryStream.forEach(change => {
     const productId = change.documentKey._id;
     const newStock = change.fullDocument.stockQuantity;
     
     // Send notification if stock is low
     if (newStock < 10) {
       sendLowStockAlert(productId, newStock);
     }
     
     // Update cache
     updateProductCache(productId, change.fullDocument);
   });
   ```

### **Q16: Implement MongoDB transactions and data consistency**
**Answer**:
1. **Multi-Document Transactions**:
   ```javascript
   // Order processing with transaction
   const session = db.getMongo().startSession();
   
   session.startTransaction({
     readConcern: { level: "snapshot" },
     writeConcern: { w: "majority" }
   });
   
   try {
     // Check product availability
     const product = db.products.findOne(
       { _id: productId, stockQuantity: { $gte: quantity } },
       { session }
     );
     
     if (!product) {
       throw new Error("Insufficient stock");
     }
     
     // Update product stock
     db.products.updateOne(
       { _id: productId },
       { $inc: { stockQuantity: -quantity } },
       { session }
     );
     
     // Create order
     const order = {
       userId: userId,
       items: [{ productId: productId, quantity: quantity, price: product.price }],
       totalAmount: product.price * quantity,
       status: "pending",
       createdAt: new Date()
     };
     
     db.orders.insertOne(order, { session });
     
     // Commit transaction
     await session.commitTransaction();
     
     return { success: true, orderId: order._id };
   } catch (error) {
     // Abort transaction on error
     await session.abortTransaction();
     throw error;
   } finally {
     session.endSession();
   }
   ```

2. **Data Consistency Patterns**:
   ```javascript
   // Optimistic concurrency control
   function updateProductWithOptimisticLock(productId, updates, expectedVersion) {
     const result = db.products.updateOne(
       {
         _id: productId,
         version: expectedVersion
       },
       {
         $set: updates,
         $inc: { version: 1 }
       }
     );
     
     if (result.modifiedCount === 0) {
       throw new Error("Concurrent modification detected");
     }
     
     return result;
   }

   // Event sourcing pattern
   function createOrderEvent(orderData) {
     const event = {
       _id: new ObjectId(),
       type: "order_created",
       data: orderData,
       timestamp: new Date(),
       version: 1
     };
     
     db.orderEvents.insertOne(event);
     
     // Update order projection
     db.orders.insertOne({
       _id: orderData._id,
       ...orderData,
       lastEventId: event._id,
       lastEventVersion: event.version
     });
   }
   ```

---

**Remember**: Focus on practical implementation, understand trade-offs, and be ready to discuss real-world scenarios and challenges.
