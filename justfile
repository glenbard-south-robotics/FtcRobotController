debug: build-debug deploy-debug
release: build-release deploy-release

build-debug:
    gradle assembleDebug
build-release:
    gradle assembleRelease

deploy-debug:
    gradle installDebug
deploy-release:
    gradle installRelease

toolchains:
    gradle javaToolchains

connect:
    adb connect 192.168.43.1:5555
