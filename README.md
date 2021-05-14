# AplicatieVaccin 

## Table of Contents
* [General description](#general-description)
* [Technologies used](#technologies-used)
* [Registering an account and logging in](#registering-an-account-and-logging-in)
* [The Manager Account](#the-manager-account)
* [The Patient Account](#the-patient-account)

## General description
AplicatieVaccin is a desktop application designed with the intent of offering patients a simple way to register for a vaccine, as well as helping hospitals and special-made centers
for vaccination to manage the whole process.

## Technologies used
* Java 15;
* JavaFx 15 - UI;
* Gradle - dependencies and build tool;
* Nitrite - database operations;
* [JavaMail](https://javaee.github.io/javamail/) - for sending informative e-mails;

## Registering an account and logging in
First of all, the user must register an account with the proper role, choosing from either:
* Patient
* Manager

Based on the role, different functions will be provided after logging in.

## The Manager Account
After logging in, the unit manager will be provided with a list of all the appointments for his unit. From this page, they can:
* **Modify available places** for the unit;
* **Cancel an appointment**, which will **send an automatic e-mail** to the patient with the reason for the cancellation;
* **Reschedule an appointment**, which will **send an automatic e-mail** to the patient with the reason and date of the reschedule;

## The Patient Account
After logging in, the patient will be assigned to an age group and provided with a list of all the available units with the number of epmpty places. From this page, they can:
* **Make an appointment** for the unit which they desire;
* **Check the appointment**, which will redirect them to a new page as well provide them with the option to **cancel the appointment**;
