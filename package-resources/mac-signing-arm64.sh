set -ex

echo Extracting internal jar files
jar -xf $2 BOOT-INF/lib/darklaf-macos-3.0.2.jar
jar -xf $2 BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar

echo Extracting dylaf dylib files
jar -xf ./BOOT-INF/lib/darklaf-macos-3.0.2.jar com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Extracting sqlite dylib files
jar -xf ./BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib

echo Signing dylaf dylib files
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.cruisepack.app.' --keychain "$1" --force com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Verify secure timestamp of dylaf dylib
/usr/bin/codesign -dvv com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Signing sqlite dylib files
/usr/bin/codesign -s 'Developer ID Application: University of Colorado Boulder (8JR6566HZ6)' -vvvv --timestamp --options runtime --prefix 'edu.colorado.cires.cruisepack.app.' --keychain "$1" --force org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib

echo Verify secure timestamp of sqlite dylib
/usr/bin/codesign -dvv org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib

echo Repacking sqlite dylib files
jar -uf ./BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar org/sqlite/native/Mac/aarch64/libsqlitejdbc.dylib

echo Repacking dylaf dylib files
jar -uf ./BOOT-INF/lib/darklaf-macos-3.0.2.jar com/github/weisj/darklaf/platform/darklaf-macos/libdarklaf-macos-arm64.dylib

echo Repacking internal jar files
jar -uf $2 BOOT-INF/lib/darklaf-macos-3.0.2.jar
jar -uf $2 BOOT-INF/lib/sqlite-jdbc-3.45.1.0.jar