Вдруг понадобится, чтобы запустить тесты:
1. pom.xml: добавить зависимости для тестконтейнера и валидации, например
   <dependency>
   <groupId>org.testcontainers</groupId>
   <artifactId>junit-jupiter</artifactId>
   <version>1.19.1</version>
   <scope>test</scope>
   </dependency>
   <dependency>
   <groupId>org.testcontainers</groupId>
   <artifactId>postgresql</artifactId>
   <version>1.19.1</version>
   <scope>test</scope>
   </dependency>
   <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   <dependency>
   <groupId>org.assertj</groupId>
   <artifactId>assertj-core</artifactId>
   </dependency>
2. В папке test должен быть создан и настроен application.properties
3. В классах UserController, User, UserServiceImpl, UserService я закомментировал код, кот. связан с аватаром.
Если не нужен, можно удалить. Также UserAvatarRepository.
4. Сценарий тестов основан на спецификации openapi.jaml, например,
   responses:
   '201':
   description: Created
   '400':
   description: Bad Request
Соответственно, проверяем в тестах ответы.
Кроме этого, проверяем эндпойнты для авторизованных и неавторизованных пользователей (пример в UserControllerTests), 
права доступа админ/пользователь в комментариях.
