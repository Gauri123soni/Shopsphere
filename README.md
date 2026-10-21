# ShopSphere

ShopSphere is a Java 17+ Maven web application packaged as a WAR. It uses JSP, Jakarta Servlets, MySQL, and JSTL.

## Requirements

- JDK 17 or newer
- Apache Maven 3.9 or newer
- MySQL Server 8 or newer
- Apache Tomcat 11

Open the project **root folder**, the folder containing `pom.xml`, in VS Code. Run Maven commands from this same folder. The nested `ShopSphere/` folder is empty and is not the project root.

## Set Up MySQL

1. Start MySQL Server.
2. Run `database/shopsphere.sql`, then `database/shopsphere-seed.sql`, in MySQL Workbench. If the MySQL command-line client is installed, run both from PowerShell:

   ```powershell
   Get-Content .\database\shopsphere.sql | mysql -u root -p
  Get-Content .\database\shopsphere-seed.sql | mysql -u root -p
   ```

3. The first script creates the database and tables. The seed script adds sample categories and products and can be run again safely. Register an account through the app. To grant admin access to that account, run this query with the email you registered:

   ```sql
   UPDATE shopsphere.users SET role = 'ADMIN' WHERE email = 'admin@example.com';
   ```

The application reads the database connection from `SHOPSPHERE_DB_URL`, `SHOPSPHERE_DB_USER`, and `SHOPSPHERE_DB_PASSWORD` (or the corresponding `shopsphere.db.*` JVM properties). Defaults are `jdbc:mysql://localhost:3306/shopsphere`, username `root`, and an empty password. If MySQL requires a password, set these variables in the same PowerShell window used to start Tomcat:

```powershell
$env:SHOPSPHERE_DB_URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC"
$env:SHOPSPHERE_DB_USER = "root"
$env:SHOPSPHERE_DB_PASSWORD = "your_mysql_password"
```

The app also accepts the JVM properties `shopsphere.db.url`, `shopsphere.db.user`, and `shopsphere.db.password`.

## Build and Run

From the project root, build the deployable WAR:

```powershell
mvn clean package
```

The output is `target/shopsphere.war`. Set `CATALINA_HOME` to your Tomcat 11 installation directory, then copy the WAR into Tomcat's `webapps` folder. For example:

```powershell
$env:CATALINA_HOME = "C:\path\to\apache-tomcat-11"
Copy-Item .\target\shopsphere.war "$env:CATALINA_HOME\webapps\shopsphere.war"
```

Start Tomcat from the same PowerShell window so it inherits the database settings above:

```powershell
& "$env:CATALINA_HOME\bin\startup.bat"
```

Open **http://localhost:8080/shopsphere/** in a browser. To stop Tomcat, run:

```powershell
& "$env:CATALINA_HOME\bin\shutdown.bat"
```

If Tomcat uses a port other than 8080, replace that port in the URL. Check the Tomcat console or `logs/` directory if startup or database connection fails.

## Demo Workflows

The app supports product browsing, customer registration and login, a session-backed cart and wishlist, checkout, profile updates, and customer order history. Checkout saves the address and order and reduces stock in one database transaction. Cart and wishlist contents last for the browser session. Card checkout is a mock option only: the application does not collect card details or contact a payment provider, and orders remain pending payment. The seed script assigns bundled local artwork to the six sample products; run it again after updating the app to refresh product image paths. Admin screens require an existing user whose `role` is `ADMIN`; the admin routes provide product and category management, order status updates, and customer activation controls.

## Project Layout

```text
database/                         MySQL schema
src/main/java/com/shopsphere/     Java application code
  controller/                     Request handlers
  dao/                            Database access
  filter/                         Request filters
  listener/                       Application listeners
  model/                           Domain objects
  service/                        Business logic
  util/                           Database and password utilities
src/main/webapp/                  JSP pages and static assets
  admin/                          Admin pages
  css/                            Stylesheets
  fragments/                      Shared JSP fragments
  images/                          Bundled SVG product and storefront artwork
  js/                             Browser scripts
  WEB-INF/                        Web deployment configuration
pom.xml                           Maven dependencies and WAR build
```