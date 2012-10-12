import java.io.File;

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.glacier.AmazonGlacierClient
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager
import com.amazonaws.services.glacier.transfer.UploadResult
import com.amazonaws.services.glacier.model.ListVaultsRequest
import com.amazonaws.services.glacier.model.ListJobsRequest
import com.amazonaws.services.glacier.model.CreateVaultRequest
import com.amazonaws.services.glacier.model.DeleteVaultRequest
import com.amazonaws.services.glacier.model.InitiateJobRequest
import com.amazonaws.services.glacier.model.JobParameters

object BigIce {

  def main(args: Array[String]) {

    if (args.length == 0) {
      printUsage
    }
    else {
      val accessKey = args(0)
      val secretKey = args(1)
      val cmd = args(2)
      
      val credentials = new BasicAWSCredentials(accessKey,secretKey);
      val client = new AmazonGlacierClient(credentials);
      client.setEndpoint("https://glacier.us-east-1.amazonaws.com/");

      cmd match {
        case "upload" => upload(client, credentials, args(3),args(4))
        case "vaults" => vaults(client)
        case "create" => create(client,args(3))
        case "delete" => delete(client,args(3))
        case "jobs" => jobs(client,args(3))
        case "inventory" => inventory(client,args(3),args(4))
        case _ => printUsage
      }
    }
  
  }

  def printUsage:Unit = {
    println ("Listing all Vaults    - java -jar big-ice.jar <AccessKey> <SecretKey> vaults")
    println ("Creating a new Vault  - java -jar big-ice.jar <AccessKey> <SecretKey> create <VaultName>")
    println ("Deleting a Vault      - java -jar big-ice.jar <AccessKey> <SecretKey> delete <VaultName>")
    println ("List Jobs for a Vault - java -jar big-ice.jar <AccessKey> <SecretKey> jobs <VaultName>")
    println ("Backup an file        - java -jar big-ice.jar <AccessKey> <SecretKey> upload <VaultName> <FileToBackup>")
    println ("Inventory for a Vault - java -jar big-ice.jar <AccessKey> <SecretKey> inventory <VaultName> <SNSTopicARN>")
  }
  
  def upload(client: AmazonGlacierClient, credentials:AWSCredentials, vaultName: String, fileName: String): Unit = {
    val atm = new ArchiveTransferManager(client, credentials);
    val result = atm.upload(vaultName, fileName, new File(fileName));
    println("Archive ID: " + result.getArchiveId());
  }

  def vaults(client: AmazonGlacierClient): Unit = {
    import collection.JavaConversions._
    val listVaultsResponse = client.listVaults(new ListVaultsRequest())
    println("Vault Name\tArchives\tSize");
    for(vault <- listVaultsResponse.getVaultList()) {
      println(vault.getVaultName()+"\t"+vault.getNumberOfArchives()+"\t"+vault.getSizeInBytes())
    }
  }

  def jobs(client: AmazonGlacierClient, vaultName: String): Unit = {
    import collection.JavaConversions._
    val listJobsResponse = client.listJobs(new ListJobsRequest(vaultName))
    println("Description\tCreated\tStatus");
    for(job <- listJobsResponse.getJobList() ) {
      println(job.getJobDescription() +"\t"+job.getCreationDate() +"\t"+job.getStatusMessage())
    }
  }

  def create(client: AmazonGlacierClient, vaultName: String): Unit = {
    val createVaultResponse = client.createVault(new CreateVaultRequest(vaultName))
    println("Vault Created: "+createVaultResponse.getLocation())
  }

  def delete(client: AmazonGlacierClient, vaultName: String): Unit = {
    client.deleteVault(new DeleteVaultRequest(vaultName))
    println("Vault Deleted: "+vaultName)
  }

  def inventory(client: AmazonGlacierClient, vaultName: String, snsTopicARN: String): Unit = {
     val initJobRequest = new InitiateJobRequest()
        .withVaultName(vaultName)
        .withJobParameters(
          new JobParameters()
            .withType("inventory-retrieval")
            .withSNSTopic(snsTopicARN)
            .withDescription("Inventory Request")
          )
     val initJobResult = client.initiateJob(initJobRequest)
     println("Inventory listing job created: "+initJobResult.getJobId())
  }

}
