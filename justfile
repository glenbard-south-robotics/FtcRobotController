build-and-deploy: build-debug deploy-debug

build-debug:
    gradle assembleDebug
deploy-debug:
    gradle installDebug
toolchains:
    gradle javaToolchains