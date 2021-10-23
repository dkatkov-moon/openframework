# Creating of message based on FreeMarker template

This example shows how properly to create complex email message with large body that depends on many inputs. In 
such cases much more convenient to keep and edit text of the body in separate file. This can be achieved using 
FreeMarker templates that are supported by Email component.

For more details about possibilities of FreeMarker templates see
 [FreeMarker Manual](https://freemarker.apache.org/docs/index.html)
 

##Configuration
All necessary configuration files can be found in <code>src/main/resources</code> directory.

**apm_run.properties**

| Parameter     | Value         |
| ------------- |---------------|
| `email.service` | Host name and port of email server |
| `email.service.protocol` | Protocol which is used by email server |
| `email.service.credentials` | Vault alias that contains credentials for authentication on the email server |
| `books.in.stock.email.recipients` | Email address where email message will be sent |

**vault.properties**

| Alias     | Value         |
| ------------- |---------------|
| `email.user` | Json with credentials in encoded with Base64. Example of json:<br>`{ "user": "sender@gmail.com", "password": "passphrase" }` |


##Running

Run `main()` method of `LocalRunner` class.