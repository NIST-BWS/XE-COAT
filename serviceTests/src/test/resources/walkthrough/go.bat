set COAT=http://zox:7774
call curl -X POST %COAT%/FizzBuzz
call curl -X POST -H "Content-Type:text/xml"  -d @default.xml %COAT%/FizzBuzz/config/default.xml
call curl -X POST -H "Content-Type:text/xml"  -d @main.xsd    %COAT%/FizzBuzz/schema/main.xsd
call curl -X POST -H "Content-Type:text/plain" -d @template.vm %COAT%/FizzBuzz/template

call curl %COAT%/FizzBuzz/process
