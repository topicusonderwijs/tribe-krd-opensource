@echo off
echo EduArte schemaupdater version 1.0
echo detecting java version:
java -version
REM echo detecting current schema version:
REM echo TODO
echo updating.....
FOR /D %%T IN (*) DO CALL %%~fT/scripts/update.bat %*
echo done.