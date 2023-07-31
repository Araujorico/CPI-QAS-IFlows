import com.sap.gateway.ip.core.customdev.util.Message

import javax.activation.DataHandler
import javax.mail.internet.ContentType
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

Message processData(Message message) {
    byte[] bytes = message.getBody(byte[])
    //  Construct Multipart
    MimeBodyPart bodyPart = new MimeBodyPart()
    ByteArrayDataSource dataSource = new ByteArrayDataSource(bytes, 'application/pdf')
    DataHandler byteDataHandler = new DataHandler(dataSource)
    bodyPart.setDataHandler(byteDataHandler)
    bodyPart.setFileName('name="files"')
    bodyPart.setDisposition('form-data; name="files"')

    MimeMultipart multipart = new MimeMultipart()
    multipart.addBodyPart(bodyPart)

    // Set multipart into body
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    multipart.writeTo(outputStream)
    message.setBody(outputStream)

    // Set Content type with boundary
    String boundary = (new ContentType(multipart.contentType)).getParameter('boundary');
    message.setHeader('Content-Type', "multipart/form-data; boundary=${boundary}")

    return message
}