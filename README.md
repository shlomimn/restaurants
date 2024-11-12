# restaurants
Which restaurant is opened now?


# pre-requests
MySQL on docker for testing

1. Create MySQL dokcer
```
docker run -d -p 3306:3306 -v /tmp/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=admin mysql
```

2. Access SQL
```
mysql -h 192.168.1.221 -P 3306 -u root -p
```

3. Create Database, Table
```
create database restaurant;
use restaurant;
CREATE TABLE restaurant_info (
    name VARCHAR(255) NOT NULL,
    style VARCHAR(50) NOT NULL,
    vegetarian TINYINT(1) NOT NULL,
    open_hour TIME NOT NULL,
    close_hour TIME NOT NULL,
    address VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);
```

4. Verify table exists
```
show tables;
+----------------------+
| Tables_in_restaurant |
+----------------------+
| restaurant_info      |
+----------------------+  
   
desc restaurant_info;
+------------+--------------+------+-----+---------+-------+
| Field      | Type         | Null | Key | Default | Extra |
+------------+--------------+------+-----+---------+-------+
| name       | varchar(255) | NO   | PRI | NULL    |       |
| style      | varchar(50)  | NO   |     | NULL    |       |
| vegetarian | tinyint(1)   | NO   |     | NULL    |       |
| open_hour  | time         | NO   |     | NULL    |       |
| close_hour | time         | NO   |     | NULL    |       |
| address    | varchar(255) | NO   |     | NULL    |       |
+------------+--------------+------+-----+---------+-------+
```

5. Add entires to table
```
INSERT INTO restaurant_info (name, style, vegetarian, open_hour, close_hour, address) VALUES
    ('Pizza Hut', 'Italian', FALSE, '09:00:00', '23:00:00', '123 Main St, New York, NY'),
    ('La Parisienne', 'French', TRUE, '08:00:00', '22:00:00', '45 Rue de Paris, Los Angeles, CA'),
    ('Sushi World', 'Japanese', FALSE, '11:00:00', '21:00:00', '789 Sakura Blvd, San Francisco, CA'),
    ('Green Eats', 'Korean', TRUE, '10:00:00', '20:00:00', '101 Greenway Rd, Chicago, IL'),
    ('Veggie Delight', 'Italian', TRUE, '10:00:00', '18:00:00', '202 Veggie St, Miami, FL'),
    ('French Feast', 'French', FALSE, '12:00:00', '22:00:00', '303 Feast Ave, Austin, TX');  

select * from restaurant_info;
+----------------+----------+------------+-----------+------------+------------------------------------+
| name           | style    | vegetarian | open_hour | close_hour | address                            |
+----------------+----------+------------+-----------+------------+------------------------------------+
| French Feast   | French   |          0 | 12:00:00  | 22:00:00   | 303 Feast Ave, Austin, TX          |
| Green Eats     | Korean   |          1 | 10:00:00  | 20:00:00   | 101 Greenway Rd, Chicago, IL       |
| La Parisienne  | French   |          1 | 08:00:00  | 22:00:00   | 45 Rue de Paris, Los Angeles, CA   |
| Pizza Hut      | Italian  |          0 | 09:00:00  | 23:00:00   | 123 Main St, New York, NY          |
| Sushi World    | Japanese |          0 | 11:00:00  | 21:00:00   | 789 Sakura Blvd, San Francisco, CA |
| Veggie Delight | Italian  |          1 | 10:00:00  | 18:00:00   | 202 Veggie St, Miami, FL           |
+----------------+----------+------------+-----------+------------+------------------------------------+  
```

Jenkins on Docker for testing

1. Create Jenkins docker
```
docker run  -d -p 8080:8080 -p 50000:50000 --restart=on-failure -v /tmp/jenkins:/var/jenkins_home jenkins/jenkins:lts-jdk17
```

When entering http://127.0.0.1:8080/
get Administrator's Jenkins password from docker
```
docker exec -it 89 bash
jenkins@89f0b24efa52:/$ cat /var/jenkins_home/secrets/initialAdminPassword
```

This password is required before suggested installation.

Then create first admin user/password to access Jenkins UI

2. Create Jobs based on Jenkins files in git repo.
   a. Query MySQL
   b. Update/Add/Remove MySQL restaurant_info table
   c. Create function(+python code), MySQL with Terraform in Azure cloud.

