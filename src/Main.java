import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double frequency;

    public Toy(int id, String name, int quantity, double frequency) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.frequency = frequency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void reduceQuantity() {
        quantity--;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", frequency=" + frequency +
                '}';
    }
}

class ToyShop {
    private List<Toy> toys;
    private List<Toy> prizeToys;

    public ToyShop() {
        toys = new ArrayList<>();
        prizeToys = new ArrayList<>();
    }

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void updateToyFrequency(int toyId, double frequency) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setFrequency(frequency);
                break;
            }
        }
    }

    public void organizePrizeDraw() {
        double totalFrequency = calculateTotalFrequency();
        if (totalFrequency == 0) {
            System.out.println("No toys available for prize draw.");
            return;
        }

        Random random = new Random();
        double randomNumber = random.nextDouble() * totalFrequency;

        double cumulativeFrequency = 0;
        for (Toy toy : toys) {
            cumulativeFrequency += toy.getFrequency();
            if (randomNumber <= cumulativeFrequency) {
                prizeToys.add(toy);
                toy.reduceQuantity();
                System.out.println("Prize toy drawn: " + toy.getName());
                break;
            }
        }
    }

    public void givePrizeToy() {
        if (prizeToys.isEmpty()) {
            System.out.println("No prize toys to give.");
            return;
        }

        Toy prizeToy = prizeToys.remove(0);
        System.out.println("Giving prize toy: " + prizeToy.getName());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("prize_toys.txt", true))) {
            writer.write(prizeToy.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    private double calculateTotalFrequency() {
        double totalFrequency = 0;
        for (Toy toy : toys) {
            totalFrequency += toy.getFrequency();
        }
        return totalFrequency;
    }
}

public class Main {
    public static void main(String[] args) {
        ToyShop toyShop = new ToyShop();

        Toy toy1 = new Toy(1, "Teddy Bear", 5, 30);
        Toy toy2 = new Toy(2, "Doll", 10, 50);
        Toy toy3 = new Toy(3, "Car", 8, 20);

        toyShop.addToy(toy1);
        toyShop.addToy(toy2);
        toyShop.addToy(toy3);

        toyShop.organizePrizeDraw();
        toyShop.organizePrizeDraw();

        toyShop.givePrizeToy();
        toyShop.givePrizeToy();

        toyShop.updateToyFrequency(1, 40);

        toyShop.organizePrizeDraw();
        toyShop.givePrizeToy();
    }
}