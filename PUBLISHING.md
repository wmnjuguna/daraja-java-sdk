# Publishing Guide: Daraja Java SDK

This guide walks you through publishing the Daraja Java SDK to Maven Central Repository.

## Overview

Your SDK is configured to publish to:
- **Maven Central** (via OSSRH - Open Source Software Repository Hosting)
- **GitHub Packages** (optional alternative)

## Prerequisites

### 1. Create Sonatype OSSRH Account

1. Go to [Sonatype JIRA](https://issues.sonatype.org/)
2. Create an account if you don't have one
3. Create a new issue with these details:
   - **Project**: Community Support - Open Source Project Repository Hosting (OSSRH)
   - **Issue Type**: New Project
   - **Summary**: Request for app.daraja.sdk groupId
   - **Group Id**: app.daraja.sdk
   - **Project URL**: https://github.com/wmnjuguna/daraja-java-sdk
   - **SCM URL**: https://github.com/wmnjuguna/daraja-java-sdk.git
   - **Username(s)**: your-sonatype-username

4. Wait for approval (usually takes 1-2 business days)

### 2. Generate GPG Key for Signing

```bash
# Generate GPG key
gpg --gen-key

# List keys to get the key ID
gpg --list-secret-keys --keyid-format LONG

# Export the private key (replace KEY_ID with your actual key ID)
gpg --armor --export-secret-keys KEY_ID

# Export the public key
gpg --armor --export KEY_ID

# Upload public key to key servers
gpg --keyserver keyserver.ubuntu.com --send-keys KEY_ID
gpg --keyserver keys.openpgp.org --send-keys KEY_ID
```

### 3. Set Up Credentials

Create `~/.gradle/gradle.properties` with your credentials:

```properties
# Sonatype OSSRH credentials
ossrhUsername=your-sonatype-username
ossrhPassword=your-sonatype-password

# GPG signing credentials
signingKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n[your-private-key-content]\n-----END PGP PRIVATE KEY BLOCK-----
signingPassword=your-gpg-passphrase
```

**Alternative: Environment Variables**
```bash
export OSSRH_USERNAME=your-sonatype-username
export OSSRH_PASSWORD=your-sonatype-password
export SIGNING_KEY="-----BEGIN PGP PRIVATE KEY BLOCK-----\n[your-private-key-content]\n-----END PGP PRIVATE KEY BLOCK-----"
export SIGNING_PASSWORD=your-gpg-passphrase
```

## Publishing Steps

### 1. Prepare for Release

```bash
# Ensure all tests pass
./gradlew test

# Build the project
./gradlew build

# Check generated artifacts
./gradlew publishToMavenLocal
```

### 2. Publish to Maven Central

```bash
# Publish to staging repository
./gradlew publish

# Alternatively, publish and release in one step (after initial setup)
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
```

### 3. Manual Release Process (First Time)

1. Go to [Sonatype Nexus Repository Manager](https://s01.oss.sonatype.org/)
2. Log in with your OSSRH credentials
3. Navigate to **Staging Repositories**
4. Find your staging repository (usually named like `appdarajasdk-XXXX`)
5. Select it and click **Close**
6. Wait for validation to complete
7. If validation passes, click **Release**
8. Your artifacts will be available in Maven Central within 2 hours

## Publishing Options

### Option 1: Maven Central (Recommended)

This is the standard approach for open-source Java libraries:

**Advantages:**
- Global accessibility
- Standard for Java ecosystem
- Automatic dependency resolution
- High trust and reliability

**Process:**
- Uses OSSRH (Sonatype)
- Requires GPG signing
- Manual approval for first-time publishers
- Automated after initial setup

### Option 2: GitHub Packages

Add this to your `build.gradle`:

```gradle
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = "https://maven.pkg.github.com/wmnjuguna/daraja-java-sdk"
            credentials {
                username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
            }
        }
    }
}
```

Then publish with:
```bash
./gradlew publishAllPublicationsToGitHubPackagesRepository
```

### Option 3: JitPack (Easiest)

JitPack builds directly from your GitHub repository:

1. Tag your release: `git tag v1.0.0 && git push origin v1.0.0`
2. Users can include your library:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.wmnjuguna:daraja-java-sdk:v1.0.0'
}
```

## Version Management

### Release Versions
```gradle
version = '1.0.0'  // Stable release
```

### Snapshot Versions
```gradle
version = '1.1.0-SNAPSHOT'  // Development version
```

## Usage After Publishing

Once published to Maven Central, users can include your SDK:

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'app.daraja.sdk:daraja-java-sdk:1.0.0'
}
```

## Automation with GitHub Actions

Create `.github/workflows/publish.yml`:

```yaml
name: Publish to Maven Central

on:
  release:
    types: [created]

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'

    - name: Setup Gradle
      uses: gradle/gradle-build-action@v2

    - name: Publish to Maven Central
      run: ./gradlew publish
      env:
        OSSRH_USERNAME: ${{ secrets.OSSRH_USERNAME }}
        OSSRH_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
        SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
        SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
```

## Troubleshooting

### Common Issues

1. **GPG Signing Fails**
   - Ensure GPG key is properly formatted
   - Check passphrase is correct
   - Verify key is uploaded to key servers

2. **OSSRH Authentication Fails**
   - Verify username/password
   - Ensure OSSRH ticket is approved
   - Check groupId matches approved request

3. **Validation Errors**
   - Ensure all required POM fields are present
   - Verify JavaDoc and sources JARs are generated
   - Check artifact signing is working

### Debug Commands

```bash
# Test signing locally
./gradlew signMavenPublication

# Check generated artifacts
./gradlew publishToMavenLocal
ls ~/.m2/repository/app/daraja/sdk/daraja-java-sdk/1.0.0/

# Verify POM content
cat ~/.m2/repository/app/daraja/sdk/daraja-java-sdk/1.0.0/daraja-java-sdk-1.0.0.pom
```

## Support

- **Sonatype Support**: https://central.sonatype.org/pages/ossrh-guide.html
- **Gradle Publishing**: https://docs.gradle.org/current/userguide/publishing_maven.html
- **GPG Documentation**: https://gnupg.org/documentation/

## Next Steps

1. Complete OSSRH registration
2. Generate and configure GPG keys
3. Test publish to staging
4. Release first version
5. Set up automated publishing with GitHub Actions

Your SDK is ready for publishing! The build configuration supports all major Maven repository options.