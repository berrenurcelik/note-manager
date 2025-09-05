/**
 * Script to run the appropriate build command based on the operating system
 */
const { execSync } = require('child_process');

// Detect the operating system
const isWindows = process.platform === 'win32';
const scriptToRun = isWindows ? 'build:win' : 'build:unix';

console.log(`Operating System: ${process.platform}`);
console.log(`Running: npm run ${scriptToRun}`);

try {
  // Execute the appropriate command
  execSync(`npm run ${scriptToRun}`, { stdio: 'inherit' });
} catch (error) {
  console.error('Build error:', error);
  process.exit(1);
}
