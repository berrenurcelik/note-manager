# Note Manager - Frontend

This is the frontend application for the Note Manager project, built with Angular 20+ and Angular Material.

## Prerequisites

- Node.js (v18 or higher)
- npm (v9 or higher)

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd NoteManager/frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```
   
   The application will be available at `http://localhost:4200/`

## Dependencies

This project uses the following key dependencies:
- Angular 20+ (Core framework)
- Angular Material (UI components)
- Angular Flex Layout (Responsive layouts)
- Angular Animations (Material animations)

All dependencies are included in `package.json` and will be installed automatically with `npm install`.

## Sample Login

The application comes with a pre-configured admin user:
- **Username:** admin
- **Password:** 123

You can also register new users through the registration form.

## Development Commands

### Start Development Server
```bash
npm start
```

### Build for Production
```bash
npm run build
```

### Run Tests
```bash
npm test
```

## Project Structure

- `src/app/` - Main application code
- `src/app/auth/` - Authentication components (login, register)
- `src/app/pages/` - Page components (notebooks, notes)
- `src/app/pages/shared/` - Shared components (dialogs, etc.)

## Troubleshooting

If you encounter "cannot find module" errors after cloning:
1. Make sure all dependencies are installed: `npm install`
2. Clear npm cache: `npm cache clean --force`
3. Delete `node_modules` and reinstall: `rm -rf node_modules && npm install`
