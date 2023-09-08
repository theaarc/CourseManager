# Course Management System

A simple web-based course management system built with Spring Boot, Spring MVC, and MySQL. This project allows users to create, view, and manage courses, including adding and managing associated documents, links, and videos.

## Features

- **Course Management:** Users can create, modify, and delete courses with titles, descriptions, and instructors.

- **Document Management:** Documents (PDFs, Word files, etc.) can be attached to courses, and users can view, add, edit, and delete documents.

- **Link Management:** Users can add and manage links related to courses.

- **Video Management:** Videos can be associated with courses, and users can manage video links.

- **Search:** A search feature allows users to find courses by keyword within titles and descriptions.

## Installation

1. Clone this repository to your local machine.

```bash
git clone https://github.com/yourusername/course-management.git
```

2. Configure your MySQL database settings in `application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/course_management
spring.datasource.username=root
spring.datasource.password=rootpassword
```

3. Build and run the project using Spring Boot.

```bash
mvn spring-boot:run
```

4. Access the application in your web browser at `http://localhost:8080`.

## Usage

- Visit the application in your web browser.

- Create new courses and add documents, links, and videos as needed.

- Search for courses using the search feature.

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests.

## Acknowledgments

- Thanks to [Spring Framework](https://spring.io/) for providing a robust framework for building Java applications.

- Special thanks to [MySQL](https://www.mysql.com/) for the database backend.

- Icon made by [Freepik](https://www.freepik.com) from [www.flaticon.com](https://www.flaticon.com/).
