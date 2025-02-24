{ pkgs }:
let
  # android-studio is not available in aarch64-darwin
  conditionalPackages = if pkgs.system != "aarch64-darwin" then [ pkgs.android-studio ] else [ ];
in
with pkgs;

# Configure your development environment.
#
# Documentation: https://github.com/numtide/devshell
devshell.mkShell {
  name = "eudiw-wallet-ui";
  motd = ''
    Entered the Android app development environment for EUDIW.
  '';
  env = [
    {
      name = "ANDROID_HOME";
      value = "${pkgs.android-sdk}/share/android-sdk";
    }
    {
      name = "ANDROID_SDK_ROOT";
      value = "${pkgs.android-sdk}/share/android-sdk";
    }
    {
      name = "JAVA_HOME";
      value = pkgs.jdk.home;
    }
  ];
  packages = with pkgs; [
    android-sdk
    gradle
    jdk
  ] ++ conditionalPackages;
}
