# Swagger Converter

Stressless Swagger to PDF/HTML Converter.

## Overview
This is a tiny java application used to convert swagger yaml file into pdf and html files.
Though there're already quite a few tools focused on swagger files' conversion, powerful and highly customizable, it is really stressful when trying to figure out every option or attribute means.  
If you just want to simply get the convert-from-yaml-to-pdf-and-html stuff done, it is for you. No programming, no options, no API documents, just one command and a peaceful world.  

## Usage
1. Prepare a machine with JRE 8+ inside.
3. Download the swaggerConverter.jar and save it into your work directory.
2. Save the yaml files into the same work directory.
4. Go to your work directory and enter `java -jar swaggerConverter.jar ./`.  

### For expert
- You can place a `.css` stylesheet file into your work directory, it will be used when generating html files.  
- The only argument stands for the path of your work directory.  

## Thanks-to
This tool used [swagger2markup](https://github.com/Swagger2Markup/swagger2markup) and [asciidoctorj](https://github.com/asciidoctor/asciidoctorj) for conversion. Other libraries used can be found in `pom.xml`.

## License
MIT
