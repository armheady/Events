# Commit Message Convention

This document provides guidelines for creating meaningful and standardized commit messages. Each commit type serves a specific purpose and helps in maintaining a clear project history.

## Commit Types

### Features
- **feat:** Commits that add or remove features
- Example: `feat: add user authentication system`

### Bug Fixes
- **fix:** Commits that fix bugs
- Example: `fix: resolve login validation issue`

### Code Refactoring
- **refactor:** Commits that rewrite/restructure code without changing API behavior
- Example: `refactor: simplify user registration logic`

### Performance Improvements
- **perf:** Special refactor commits that improve performance
- Example: `perf: optimize database queries`

### Style Changes
- **style:** Commits that don't affect code behavior (whitespace, formatting, etc.)
- Example: `style: format user controller code`

### Tests
- **test:** Commits that add or correct tests
- Example: `test: add unit tests for user service`

### Documentation
- **docs:** Commits that affect documentation only
- Example: `docs: update API endpoints documentation`

### Build System
- **build:** Commits that affect build components
- Includes: build tools, CI pipeline, dependencies, project version
- Example: `build: upgrade spring-boot version`

### Operations
- **ops:** Commits that affect operational components
- Includes: infrastructure, deployment, backup, recovery
- Example: `ops: configure production deployment pipeline`

### Miscellaneous
- **chore:** Commits that don't modify src or test files
- Example: `chore: update .gitignore file`

## Best Practices

1. Start with the type of the change
2. Use imperative mood in the subject line
3. Provide a brief description of the change
4. Reference relevant issue numbers when applicable

## Examples

```
feat: implement OAuth2 authentication
fix: resolve null pointer in user profile
refactor: restructure event handling logic
perf: optimize image loading
style: apply consistent indentation
test: add integration tests for payment system
docs: update README with setup instructions
build: update dependencies to latest versions
ops: configure backup automation
chore: update IDE configuration
```