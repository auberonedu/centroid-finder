export default {
    testEnvironment: 'node',
    transform: {},
    testMatch: ['**/tests/**/*.test.js'],
    moduleFileExtensions: ['js', 'json', 'node'],
    // tell Jest to not transform the files (including serverController.js)
    transformIgnorePatterns: [],
  };
  