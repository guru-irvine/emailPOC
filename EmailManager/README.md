# Email Manager

A Java application for managing emails using the Microsoft Graph API. This application allows you to download emails, view conversation threads, and reply to emails.

## Quick Start

```bash
# Clone the repository
git clone https://github.com/guru-irvine/emailPOC.git
cd emailPOC/EmailManager

# Copy and edit configuration
cp run.sh.example run.sh
nano run.sh  # Add your Azure AD credentials

# Build and run
chmod +x run.sh
./run.sh
```

## Prerequisites

- Java 17 or later
- Maven 3.9 or later
- Microsoft 365 account with appropriate permissions
- Azure AD application registration with the following:
  - Client ID
  - Client Secret
  - Tenant ID

### Required Microsoft Graph API Permissions

Your Azure AD application needs these permissions:
- `Mail.Read`
- `Mail.ReadWrite`
- `Mail.Send`
- `User.Read`

## Setup

1. Clone the repository
2. Configure the application using one of these methods:
   - Use the provided shell script:
     ```bash
     # 1. Copy run.sh.example to run.sh
     cp run.sh.example run.sh
     
     # 2. Edit run.sh with your credentials
     nano run.sh  # or use your preferred editor
     
     # 3. Make it executable and run
     chmod +x run.sh
     ./run.sh
     ```
   - Set environment variables:
     ```bash
     export EMAIL_MANAGER_CLIENT_ID="your-client-id"
     export EMAIL_MANAGER_CLIENT_SECRET="your-client-secret"
     export EMAIL_MANAGER_TENANT_ID="your-tenant-id"
     export EMAIL_MANAGER_USER_EMAIL="your-email@domain.com"
     ```
   - Or update `config.json` with your Azure AD application credentials:
     ```json
     {
         "clientId": "your-client-id",
         "clientSecret": "your-client-secret",
         "tenantId": "your-tenant-id",
         "userEmail": "your-email@domain.com"
     }
     ```

3. Build the application:
   ```bash
   mvn clean package
   ```

4. Run the application:
   ```bash
   java -jar target/emailmanager.jar
   ```

## Features

1. Download new emails and save to CSV and .eml files
2. Reply to emails
3. List all stored emails
4. View conversation threads with full email content
5. Automatic duplicate email detection
6. Chronological conversation threading

## Project Structure

```
EmailManager.Java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── emailmanager/
│   │   │           ├── App.java
│   │   │           ├── models/
│   │   │           │   ├── EmailConfig.java
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
└── README.md
```

## Dependencies

- Microsoft Graph SDK for Java
- Azure Identity
- OpenCSV (CSV file handling)
- Jackson (JSON processing)
- SLF4J and Logback (logging)
- JUnit Jupiter (testing)

## Storage Structure

- `emails/emails.csv`: Contains email metadata including:
  - Message ID
  - Subject
  - Received date/time
  - Sender
  - Internet Message ID
  - Conversation ID
  - References
  - Message type (Original/Reply)

- `emails/storage/*.eml`: Contains individual email content files with:
  - Headers (From, Subject, Date, etc.)
  - Message IDs and References
  - Conversation threading information

## Troubleshooting

### Common Issues

1. **Authentication Failed**
   - Verify your Azure AD credentials in config.json or environment variables
   - Check if your Azure AD application has the required permissions
   - Ensure the permissions are admin-consented if required

2. **Email Download Issues**
   - Check your internet connection
   - Verify the user email address matches the authenticated account
   - Ensure the mailbox is accessible

3. **Build Failures**
   - Ensure Java 17 or later is installed: `java -version`
   - Verify Maven installation: `mvn -version`
   - Try cleaning the project: `mvn clean`

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Notes

- Emails are stored in both CSV format (metadata) and .eml files (content)
- The application uses environment variables or config.json for credentials
- Conversation threading is implemented using message headers and conversation IDs
- Duplicate emails are automatically detected and skipped
- All dates are stored in ISO-8601 format with timezone information 