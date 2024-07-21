FROM python:3

# Copy the application files
COPY ./app/ /app

# Copy the requirements file
COPY requirements.txt /app/requirements.txt

# Set the working directory
WORKDIR /app

# Install dependencies
RUN pip install -r requirements.txt

# Expose the required port
EXPOSE 9229

# Create a group and user for the container
RUN groupadd -r Hackfest_ctf && useradd -r -g Hackfest_ctf ctf

# Change ownership of the application directory
RUN chown -R ctf:Hackfest_ctf /app
# Restrict permissions to read-only for the user
RUN chmod -R 555 /app


# Switch to the restricted user
USER ctf:Hackfest_ctf

# Command to run the application
CMD ["python3", "-u", "app.py"]
