# Java Email Manager Documentation

## Introduction
The Java Email Manager is an application designed to manage emails using the Microsoft Graph API. It allows users to download, reply to, and organize emails while maintaining conversation threads. The application is built using Java and Maven, ensuring a robust and scalable architecture.

## Project Structure
The project is organized into the following key directories:

```
java/
└── EmailManager.Java/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/
    │   │   │       └── emailmanager/
    │   │   │           ├── App.java
    │   │   │           ├── config/
    │   │   │           │   └── EmailConfig.java
    │   │   │           ├── models/
    │   │   │           │   └── EmailRecord.java
    │   │   │           ├── services/
    │   │   │           │   ├── ConfigService.java
    │   │   │           │   ├── EmailStorageService.java
    │   │   │           │   └── GraphService.java
    │   │   │           └── utils/
    │   │   │               └── OffsetDateTimeConverter.java
    │   │   └── resources/
    │   │       ├── config.json
    │   │       └── logback.xml
    │   └── test/
    │       └── java/
    │           └── com/
    │               └── emailmanager/
    │                   └── services/
    │                       └── EmailStorageServiceTest.java
    ├── pom.xml
    ├── README.md
    └── documentation.md
```

## File Descriptions

- **App.java**: The main entry point of the application. It initializes the application and handles the execution flow.
  
- **config/EmailConfig.java**: Contains configuration settings for the application, including Azure AD credentials and other necessary parameters.

- **models/EmailRecord.java**: Defines the structure of an email record, including fields for metadata such as subject, sender, and conversation ID.

- **services/ConfigService.java**: Manages configuration loading and provides access to application settings.

- **services/EmailStorageService.java**: Handles the storage and retrieval of email records, including saving emails in both CSV and .eml formats.

- **services/GraphService.java**: Interacts with the Microsoft Graph API to perform operations such as downloading emails and sending replies.

- **utils/OffsetDateTimeConverter.java**: Provides functionality to convert `OffsetDateTime` objects to and from CSV-compatible string formats.

- **resources/config.json**: Configuration file for the application, containing settings for Azure AD authentication.

- **resources/logback.xml**: Logging configuration file for setting up logging behavior in the application.

- **test/services/EmailStorageServiceTest.java**: Contains unit tests for the `EmailStorageService`, ensuring that email storage functionality works as expected.

## How to Run the Application
1. Clone the repository.
2. Navigate to the Java project directory:
   ```bash
   cd java/EmailManager.Java
   ```
3. Set up your environment variables or update `config.json` with your Azure AD credentials.
4. Run the application using the provided shell script:
   ```bash
   ./run.sh
   ```

## Configuration
The application requires the following environment variables to be set:
- `EMAIL_MANAGER_CLIENT_ID`: Your Azure AD application client ID.
- `EMAIL_MANAGER_CLIENT_SECRET`: Your Azure AD application client secret.
- `EMAIL_MANAGER_TENANT_ID`: Your Azure AD tenant ID.
- `EMAIL_MANAGER_USER_EMAIL`: Your Microsoft 365 email address.

### Logging Configuration
The application uses Logback for logging, and the configuration is defined in `logback.xml`. Logs are stored in the `logs` subfolder of the project directory. The logging configuration includes:
- A console appender for real-time logging output.
- A file appender that writes logs to `logs/emailmanager.log`, with a rolling policy to manage log file sizes and retention.

## Features
- Download new emails and save them in CSV and .eml formats.
- Reply to emails directly through the application.
- List all stored emails and view conversation threads.
- Automatic detection of duplicate emails to prevent storage of redundant data.

## Testing
To run the unit tests, execute the following command in the project directory:
```bash
mvn test
```

## Contributing
Contributions to the project are welcome! Please fork the repository and submit a pull request with your changes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
