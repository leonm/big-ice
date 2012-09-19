# Big Ice (Amazon Glacier Command Line Tool)

Big Ice lets you interact with Amazon Glacier from a command line shell.  The main goal is to make backing up to Amazon Glacier as easy as possible.

## Download

You will need a Java JRE 1.5 later to run Big Ice.

Download the [big-ice.jar](https://s3.amazonaws.com/big-ice/big-ice.jar)

## Getting Started

All Big Ice commands require an Amazon Access Key and Secret Key.  You can get those at your [Amazon Security Credentials](https://portal.aws.amazon.com/gp/aws/securityCredentials)

Listing All Vaults    - java -jar big-ice.jar <AccessKey> <SecretKey> vaults

Creating a New Vault  - java -jar big-ice.jar <AccessKey> <SecretKey> create <VaultName>

Deleting a Vault      - java -jar big-ice.jar <AccessKey> <SecretKey> delete <VaultName>

List Jobs For a Vault - java -jar big-ice.jar <AccessKey> <SecretKey> jobs <VaultName>

Backup a File        - java -jar big-ice.jar <AccessKey> <SecretKey> upload <VaultName> <FileToBackup>


## Contributing

You will need to install Simple Build Tool for Scala to be able to build Big Ice.

Clone on Github and run sbt assembly to package a new big-ice.jar