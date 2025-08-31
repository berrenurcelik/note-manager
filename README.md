# Note Manager

A full-stack note management application built with Spring Boot (backend) and Angular (frontend).

## Project Overview

This application allows users to:
- Create and manage notebooks
- Add, edit, and delete notes within notebooks
- User authentication (login/register)
- Organize notes with a modern Material Design interface

## Architecture

- **Backend**: Spring Boot 3.5.4 with Kotlin
- **Frontend**: Angular 20+ with Angular Material
- **Database**: H2 in-memory database (for development)
- **Authentication**: JWT-based authentication

## Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- npm 9 or higher

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd NoteManager
   ```

2. **Install frontend dependencies**
   ```bash
   cd frontend
   npm install
   cd ..
   ```

3. **Start both backend and frontend**
   ```bash
   npm start
   ```

   This will start:
   - Backend server on `http://localhost:8080`
   - Frontend application on `http://localhost:4200`

### Sample Login

The application comes with a pre-configured admin user:
- **Username:** admin
- **Password:** 123

You can also register new users through the registration form.

## Development

### Backend (Spring Boot)

```bash
cd backend
./gradlew bootRun
```

The backend API will be available at `http://localhost:8080`

### Frontend (Angular)

```bash
cd frontend
npm start
```

The frontend will be available at `http://localhost:4200`

## Project Structure

```
NoteManager/
├── backend/                 # Spring Boot application
│   ├── src/main/kotlin/     # Kotlin source code
│   ├── src/main/resources/  # Configuration files
│   └── build.gradle.kts     # Gradle build file
├── frontend/                # Angular application
│   ├── src/app/            # Angular components and services
│   ├── package.json        # npm dependencies
│   └── angular.json        # Angular configuration
└── package.json            # Root package.json for unified scripts
```

## Key Features

- **User Authentication**: Secure login and registration
- **Notebook Management**: Create, edit, and delete notebooks
- **Note Management**: Add, edit, and delete notes within notebooks
- **Responsive Design**: Modern Material Design interface
- **Real-time Updates**: Dynamic content management

## API Endpoints

The backend provides RESTful APIs for:
- User authentication (`/api/auth/*`)
- Notebook management (`/api/notebooks/*`)
- Note management (`/api/notes/*`)

## Troubleshooting

### Common Issues

1. **"Cannot find module" errors**
   - Run `npm install` in the frontend directory
   - Make sure all dependencies are properly installed

2. **Backend not starting**
   - Ensure Java 17+ is installed
   - Check if port 8080 is available

3. **Frontend build errors**
   - Clear npm cache: `npm cache clean --force`
   - Delete node_modules and reinstall: `rm -rf node_modules && npm install`

4. **Database issues**
   - The H2 database is in-memory and resets on restart
   - Sample data is automatically loaded on startup

## Documentation Generation

This project includes documentation for both frontend and backend components.

### Frontend Documentation (TypeDoc)

The frontend uses TypeDoc to generate documentation from TypeScript code and JSDoc comments.

#### Generate Frontend Documentation:
```bash
cd frontend
npm run docs
```

The documentation will be generated in `frontend/docs/` directory and can be viewed by opening `frontend/docs/index.html` in a web browser.

#### View Generated Documentation:
```bash
cd frontend
npm run docs:serve
```
This will start a local server to view the documentation at `http://localhost:8080`.

### Backend Documentation (Dokka)

The backend uses Dokka to generate documentation from Kotlin code and KDoc comments.

#### Generate Backend Documentation:
```bash
cd backend
./gradlew dokkaHtml
```

The documentation will be generated in `backend/build/docs/kdoc/` directory and can be viewed by opening `backend/build/docs/kdoc/index.html` in a web browser.

### Viewing Documentation

- **Frontend Docs**: Open `frontend/docs/index.html` in your browser
- **Backend Docs**: Open `backend/build/docs/kdoc/index.html` in your browser

Both documentation sets include:
- API documentation
- Code structure and organization
- Method and class descriptions
- Parameter and return type information

### Documentation in Version Control

- Frontend documentation is included in the repository (`frontend/docs/`)
- Backend documentation is included in the repository (`backend/build/docs/kdoc/`)
- Both documentation sets are kept up-to-date with the codebase

## Development Notes

- **Database**: Uses H2 in-memory database that resets on each startup
- **Authentication**: JWT-based authentication system
- **Cross-Origin**: Frontend and backend are configured to work together via proxy
- **Material Design**: UI components use Angular Material
- **Code Quality**: Both frontend and backend include comprehensive documentation



