
#!/bin/bash

# THe issue is that the Maven Release Plugin insists on creating a tag, and git-flow also wants to create a tag.
# Secondly, the Maven Release Plugin updates the version number to the next SNAPSHOT release before you can
# merge the changes into master, so you end with the SNAPSHOT version number in master, and this is highly undesired.
# This script solves this by doing changes locally, only pushing at the end.
# All git commands are fully automated, without requiring any user input.
# See the required configuration options for the Maven Release Plugin to avoid unwanted pushs.

# Based on the excellent information found here: http://vincent.demeester.fr/2012/07/maven-release-gitflow/

# CHANGE THESE BEFORE RUNNING THE SCRIPT!
# The version to be released
releaseVersion=0.0.2
# The next development version
developmentVersion=0.0.3-SNAPSHOT
# Provide an optional comment prefix, e.g. for your bug tracking system
scmCommentPrefix='GS1: '

# The Maven release
mvn --batch-mode release:prepare release:perform -DscmCommentPrefix="$scmCommentPrefix" -DreleaseVersion=$releaseVersion -DdevelopmentVersion=$developmentVersion

# Start the release by creating a new release branch
git checkout -b release/$releaseVersion gps103-to-owntracks-app-$releaseVersion

# go to the master branch
git checkout master

# Finally push everything
git push --all && git push --tags