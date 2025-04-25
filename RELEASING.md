## Releasing

In the following sections it will be explained the steps necessary to release a new version of the libraries. In all steps the
version to be released is referred to as `<version-to-release>`, which should be replaced with appropriate version number
(e.g. 0.26.0).

To release a new version:
  1. Submit a PR with the following changes:
    1. Update version in:
       1. `build.gradle` file (e.g. remove `-SNAPSHOT`);
       2. `CHANGELOG.md` file (e.g. replace `[Unreleased]` with `[<version-to-release>] - <todays-date>`)
       3. source code (e.g. `@since` and `@deprecated` JavaDoc tags);
  2. Wait for the PR to be approved and merged
  3. Checkout `main` and tag the new version:
     `git tag -s <version-to-release> -m "Version <version-to-release>"`
  4. Push the tag:
     `git push upstream <version-to-release>`

(Assuming `upstream` is the zaproxy repo.)

For an example see https://github.com/zaproxy/zest/pull/304

### Build for Release

Checkout the tagged version:

    git checkout <version-to-release>

Create the artifacts/libraries necessary for the release:

    ./gradlew clean build

### Release to Maven Central

To upload the built artifacts to OSSRH you can run the following:

    ./gradlew publish

Once uploaded continue with the release process in OSSRH:
http://central.sonatype.org/pages/releasing-the-deployment.html

NOTE: The following properties must be defined (e.g. in file `GRADLE_HOME/gradle.properties` ) to successfully sign and
upload the artifacts:
 - `signing.keyId` - the ID of the GPG key, used to sign the artifacts;
 - `ossrhUsername` - the OSSRH username;
 - `ossrhPassword` - the OSSRH password for above username.

Also, the user must have permissions to upload to GroupId `org.zaproxy`.

### Bump the Version

Submit a PR with the following Changes:
  1. Update version in:
     1. `build.gradle` file (e.g. increment version and add `-SNAPSHOT`);
     2. `CHANGELOG.md` file (e.g. add `[Unreleased]` sections at the top and bottom of the file
     
 For an example see https://github.com/zaproxy/zest/pull/305
 
