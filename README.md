## Convert a Microsoft Access database to a series of CSV files

Very simple little Java based converter using the `jackcess` library.

## Build

Uses gradle

```
gradle clean build
```

your java JAR with `/bin/bash` script are located in `dist`

## Run / use

```
cd dist/
./convert-access.sh /path/to/access.accdb
```

