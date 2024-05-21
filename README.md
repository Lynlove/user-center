# 项目简介
## 项目介绍
帮助大家找到学习伙伴的移动端 H5 网站(APP 风格)，
使用 vue3+springboot + mybatis-plus+redis +redission开发的移动端平台，支持用户按自选标签 匹配相似爱好用户，实现了用户管理、标签检索、用户推荐、实时组队等功能。
项目选型 : Spring Boot，SSM，Mybatis Plus，Spring Security，Redis，Spring Cache，swagger，Linux，Nginx

使用 Session结合全局拦截器进行用户登录验证，并利用Redis实现 分布式Session，解决集群间 登录态同步问题;对保存的用户信息进行脱敏处理以保证用户数据安全。
使用 Redis缓存首页高频访问的用户信息列表，有效缩短接口响应时长，且通过自定义Redis序列化器来解决数据乱码、空间浪费的问题。
为解决首次访问系统主页推荐用户列表加载过慢的问题，使用 Spring Scheduler定时任务 将待加载数据存入redis来实现缓存预热，并通过 分布式锁 保证多机部署时定时任务不会重复执行。
为解决用户重复加入队伍、入队人数超限的问题，使用 Redisson分布式锁 来实现操作互斥，保证了接口幂等性。
使用 编辑距离算法 实现了根据标签匹配最相似用户的功能，并使用Stream API对集合进行排序、映射等操作，降低代码编写复杂度。
![image](https://github.com/Lynlove/user-center/assets/64353629/817f7e06-5334-4f36-aa77-9b2385ab36f4)
![image](https://github.com/Lynlove/user-center/assets/64353629/4915bee5-93c3-4d8d-80ad-c7c503347b6a)
![image](https://github.com/Lynlove/user-center/assets/64353629/15d65e69-90c9-4b15-885f-95f516a9eb83)
![image](https://github.com/Lynlove/user-center/assets/64353629/96379cf5-700d-4ec9-8df6-390b70f51b6d)
![image](https://github.com/Lynlove/user-center/assets/64353629/61dac605-62da-47c9-8fe4-1ca679bb2cb2)
