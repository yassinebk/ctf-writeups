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
