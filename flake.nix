{
  description = "A flake for Android development";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-23.11";

    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachSystem [ "x86_64-linux" ] (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config = {
            allowUnfree = true;
            android_sdk.accept_license = true;
          };
        };
        androidComposition = pkgs.androidenv.composeAndroidPackages {
          cmdLineToolsVersion = "8.0";
          toolsVersion = "26.1.1";
          platformToolsVersion = "34.0.5";
          buildToolsVersions = [ "33.0.1" ];
          includeEmulator = true;
          emulatorVersion = "34.1.9";
          platformVersions =
            [ "24" "25" "26" "27" "28" "29" "30" "31" "32" "33" "34" ];
          includeSystemImages = true;
          systemImageTypes = [ "google_apis_playstore" ];
          abiVersions = [ "x86_64" ];
          includeExtras = [ "extras;google;gcm" ];
        };
      in {
        devShells.default = pkgs.mkShell {
          buildInputs = [
            androidComposition.androidsdk
            pkgs.android-studio
            pkgs.openjdk17
          ];
          shellHook = ''
            export ANDROID_HOME="${androidComposition.androidsdk}/libexec/android-sdk"
            export JDK_HOME="${pkgs.openjdk17.home}"
            export JAVA_HOME="${pkgs.openjdk17.home}"
            export PATH="$PATH":$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
          '';
        };
      });
}
