echo "Corriendo clientes..."

for i in {0..100}
do
  java -jar client/build/libs/client.jar &
done