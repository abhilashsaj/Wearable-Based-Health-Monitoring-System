#to create pdf report
from io import BytesIO
from reportlab.pdfgen import canvas
from django.http import HttpResponse
#to automate email
import email, smtplib, ssl
from email import encoders
from email.mime.base import MIMEBase
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText