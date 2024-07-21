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
