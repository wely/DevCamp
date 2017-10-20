FROM microsoft/dotnet:1.1-runtime
ARG source
ENV DEVICE_NAME "myFirstDevice"
ENV DEVICE_KEY "abc123"
ENV DEVICE_LATITUDE "32.924276"
ENV DEVICE_LONGITUDE "-97.295136"
WORKDIR /app
COPY ${source:-obj/Docker/publish} .
ENTRYPOINT ["dotnet", "CoreSimulatedDevice.dll"]
