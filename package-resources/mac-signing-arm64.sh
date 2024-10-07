set -ex

echo Extracting internal jar files
jar -xf ./jars/cruise-pack-1.0.0-SNAPSHOT-exe.jar BOOT-INF/lib/darklaf-macos-3.0.2.jar
jar -xf ./jars/cruise-pack-1.0.0-SNAPSHOT-exe.jar BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar

echo Extracting dylaf dylib files
ls .jars
jar -xf ./jars/BOOT-INF/lib/darklaf-macos-3.0.2.jar com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Extracting sqlite dylib files
jar -xf ./jars/BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib

echo Signing dylaf dylib files
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.cruisepack.app.' --keychain "$1" --force com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Signing sqlite dylib files
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.cruisepack.app.' --keychain "$1" --force org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib


echo Repacking sqlite dylib files
jar -uf ./jars/BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib

echo Repacking dylaf dylib files
jar -uf ./jars/BOOT-INF/lib/darklaf-macos-3.0.2.jar com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Repacking internal jar files
jar -uf ./jars/cruise-pack-1.0.0-SNAPSHOT-exe.jar BOOT-INF/lib/darklaf-macos-3.0.2.jar
jar -uf ./jars/cruise-pack-1.0.0-SNAPSHOT-exe.jar BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar