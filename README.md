# Library Management System

A Java web application (Spring Boot + Thymeleaf + MySQL) for managing books,
members, and borrowing/returning, built for a lab final project. It supports
three kinds of login accounts - Student, Teacher, and Librarian - each with
different permissions.

## OOP concepts used

| Concept        | Where |
|---|---|
| Encapsulation  | All entity fields are `private`, changed only through getters/setters or controlled methods like `Book.borrowOneCopy()`. Passwords are stored only as BCrypt hashes. |
| Abstraction    | `Person` is an abstract class; `LibraryService` hides the borrowing/fine rules behind simple method calls (`issueBook`, `returnBook`); Spring Security only ever talks to the `UserDetailsService` interface. |
| Inheritance    | `Student`, `Teacher`, and `Librarian` all extend `Person`, reusing its identity and login fields instead of duplicating them. |
| Polymorphism   | `person.getMaxBorrowLimit()` / `getBorrowDurationDays()` / `getMemberType()` / `getRole()` each behave differently depending on whether `person` is actually a `Student`, `Teacher`, or `Librarian` - used both for borrowing rules and for login/role assignment. |

## Project layout

```
src/main/java/com/library/
  model/        Book, Person (abstract), Student, Teacher, Librarian, BorrowRecord
  repository/   Spring Data JPA repositories
  service/      LibraryService (issue/return/fine logic), BookService, MemberService
  controller/   Web (Thymeleaf) controllers
src/main/resources/
  templates/    HTML pages (Thymeleaf)
  static/css/   Stylesheet
```

## Login & roles

The app now has three kinds of accounts, all logging in through the same `/login`
page but with different permissions:

| Role | Who | Access |
|---|---|---|
| **LIBRARIAN** | Staff | Full access - manage books, members, staff, issue/return books |
| **TEACHER** | Patron | Read-only - dashboard, book catalogue, list of borrowed books |
| **STUDENT** | Patron | Same as Teacher (read-only) |

All three are backed by one `persons` table (id, name, email, phone, username,
password) with `students` / `teachers` / `librarians` tables holding the
type-specific fields - this is the same OOP inheritance structure used for the
borrowing rules, just extended to cover login and role as well (see `Person.java`).

Default librarian account (change the password after first login):

- **Username:** `librarian`
- **Password:** `librarian123`

Seeded student/teacher demo accounts (from `sql/seed_members.sql`) all share the
password `changeme123`, with usernames like `student001`, `teacher031`, etc.

### Creating the librarian account (one-time, after first deploy)

The `persons`/`librarians` tables are created automatically by Hibernate the first
time the app starts. After that, seed the default librarian account:

```bash
wget -O seed_librarian.sql https://raw.githubusercontent.com/git-shahinalom/library-management-system/main/sql/seed_librarian.sql
sudo mysql < seed_librarian.sql
```
(run this on VM 103, same as the book/member seed scripts)

### Changing a password

Generate a new BCrypt hash for the new password (e.g. with Python):
```bash
pip install bcrypt --break-system-packages
python3 -c "import bcrypt; print(bcrypt.hashpw(b'YOUR_NEW_PASSWORD', bcrypt.gensalt(rounds=10)).decode())"
```
Then update it in MySQL on VM 103:
```sql
UPDATE persons SET password='PASTE_HASH_HERE' WHERE username='librarian';
```

## Running locally (your own laptop, for development)

1. Install JDK 17+ and MySQL locally.
2. Create a database: `CREATE DATABASE library_db;`
3. Edit `src/main/resources/application.properties` with your local MySQL
   username/password.
4. Run:
   ```
   mvn spring-boot:run
   ```
5. Open http://localhost:8080

## Deploying to the VM setup (VM 101 = app, VM 103 = database)

This matches our infrastructure: VM 103 is the dedicated Database Server,
VM 101 is the Web Server (already running the `firojatech-system` site via
Docker on ports 3000/8080/3306 - this app uses different ports so it runs
alongside it without conflicts).

### 1. One-time setup on VM 103 (database)

```bash
scp sql/init-vm103.sql user@192.168.88.252:~
ssh user@192.168.88.252
# edit init-vm103.sql and set a real password first
sudo mysql < init-vm103.sql
```

Make sure MySQL accepts connections from VM 101 (bind-address `0.0.0.0` in
`/etc/mysql/mysql.conf.d/mysqld.cnf`, then `sudo systemctl restart mysql`),
and that the firewall allows it:
```bash
sudo ufw allow from 192.168.88.254 to any port 3306
```

### 2. Push this project to GitHub, then clone it onto VM 101

```bash
git clone https://github.com/<your-org>/library-management-system.git
cd library-management-system
```

### 3. Set the database password (kept out of git)

```bash
cp .env.example .env
nano .env   # put the real DB_PASSWORD here
```

### 4. Build and run with Docker

```bash
docker compose up -d --build
```

The app will be available at `http://192.168.88.254:8081`.

### 5. Updating later

```bash
git pull
docker compose up -d --build
```

## Notes

- Ports used on VM 101: `8081` (app). This does not touch `firojatech-system`'s
  `3000`, `8080`, or `3306`.
- The database lives entirely on VM 103; VM 101 has no local MySQL container.
