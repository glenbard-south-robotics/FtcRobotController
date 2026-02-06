{
  description = "FTC Development Environment";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = { self, nixpkgs }: {
    devShells.x86_64-linux.default = let
      pkgs = import nixpkgs { system = "x86_64-linux"; };
    in pkgs.mkShell {
      packages = with pkgs; [
        jdk17
        jdk8
        gradle
      ];

      JAVA_HOME = pkgs.jdk17;
      JAVA_HOME_8 = pkgs.jdk8;

      shellHook = ''
        export PATH=${pkgs.jdk17}/bin:$PATH
        echo "---"
        echo "JAVA_HOME=$JAVA_HOME"
        echo "JAVA_HOME_8=$JAVA_HOME_8"
        echo "---"
        java -version
      '';
    };
  };
}
