ПОШАГОВАЯ ПОДГОТОВКА ТЕСТОВОГО ОКРУЖЕНИЯ
!!! В коде тестов ничего менять не нужно. Если не проходят - проверьте по шагам ниже.
Текст ниже - это скорее журнал для поиска проблемы для меня самого, чем инструкции для вас, т.к. все всем известно,
но что-то забывается...

Предлагаю всем у кого есть время попробовать запустить тесты на своих компьютерах. Может быть сразу несколько причин, 
почему у меня тесты работают, а у вас нет. Когда я начал делать ДЗ по тестам то столкнулся с системными проблемами (У меня
Windows 11 и нужно следить за правами доступа к запускаемым приложениям...). 
Пришлось потратить много времени, чтобы разобраться почему ничего не работает в Docker. На ваших компах могут быть другие
настройки окружения. 
Также тесты могут не пройти, если есть ошибки в коде приложения.
Возможно следующие шаги помогут сэкономить время и запустить тесты.
1. Клонировал версию нашего проекта от 6 ноября из ветки main (КЛОНИРОВАЛ, а не подтянул изменения. Если подтянуть, думаю
тоже должно сработать, но я не пробовал)
Maven потребует Rebuil Project.
Сделал Maven->LifeCycle-> сначала clean (ошибок нет) затем install (будет сообщение об ощибках в тестах, т.е. базовый 
тест не прошел, т.к. еще ничего не менял в application.properties)
2. Создал новую ветку от ветки main. Далее все делал в новой ветке.
В файле resources/application.properties устанавливаю свой пароль в строке
   spring.datasource.password=Wertyrwer -> spring.datasource.password=ya030423 (это как обычно мы делаем)
Запустил приложение -> выполнилось без ошибок.
Выполнил Maven compile -> BUILD SUCCESS (и несколько желтых WARNING в начале)
Выполнил Maven test -> Tests run: 1, Failures: 0, Errors: 0, Skipped: 0 -> BUILD SUCCESS (теперь базовый тест выполнился
без ошибок, это означает, что окружение приложения настроено правильно и работает. ВАЖНО - Docker базовому тесту не нужен
и на этом этапе я его не запускал)
Можно еще раз проверить базовый тест, запустив тест напрямую из класса HomeworkApplicationTests (папка test). Тест
работает: Process finished with exit code 0
3. Подключаю реальную БД к тестам:
- В папке test создать директорию resources
- в resources создать файл application.properties
- в этот файл скопировать все строки из main/java/resources/application.properties
- строку spring.datasource.driver-class-name=org.postgresql.Driver удалить или закомментировать.
- добавить строку spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
4. Добавить зависимости в pom.xml (Не забывайте нажимать кнопку Load Maven Changes)
-      <dependency>
          <groupId>org.testcontainers</groupId>
          <artifactId>junit-jupiter</artifactId>
          <version>1.19.1</version>
          <scope>test</scope>
      </dependency>
-         <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>1.19.1</version>
            <scope>test</scope>
        </dependency>
- удалить повторяющуюся зависимость spring-boot-starter-security (у себя я ее закомментировал)
- на всякий случай запустите приложение (можно забыть про Load Maven Changes и т.п) или проверить сборку в Maven compile
д.б. BUILD SUCCESS 
- Если запустить базовый тест в Maven, то он проходит [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
- В директориии test/java/ru.skypro.homework создать класс DatabaseConnectionTest (или этот класс подтянется, если
сделаете пулл ветки. Код в этом классе я задокументировал, чтобы тесты не запускались с ошибкой без подключенной БД
и нужных зависимостей и не тратили время)
- скопировать библиотеки и код тестов БД в класс DatabaseConnectionTest из моей ветки на гитхабе
- Пробуем запустить прямо из класса один из двух тестов подключения к БД (в принципе они одинаковые).
- Если Docker уже запущен, то тест должен выполниться и быть зеленым. 
Если Docker не запущен, видим в начале логов 
  As no valid configuration was found, execution cannot continue, 
а в ошибках красным java.lang.IllegalStateException: Failed to load ApplicationContext, что означает, что тест запущен без Docker.
- Запустить Docker Desktop, затем снова запустить любой тест из DatabaseConnectionTest.
- Если тест не проходит и видете ошибку
  org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource
и красным java.lang.IllegalStateException: Failed to load ApplicationContext - то
значит в test application.properties не удалили(закомментировали) строку spring.datasource.driver-class-name=org.postgresql.Driver
- если любой из тестов DatabaseConnectionTest прошел, можно запустить оба теста, нажав кнопку около заголовка класса,
а также выполнив команду в Maven     test
В логах увидим [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
3 теста - это базовый тест и 2 теста из DatabaseConnectionTest