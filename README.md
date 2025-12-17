# NFTRankSorter

**NFTRankSorter** is an Android application designed to analyze, calculate, and rank the rarity of NFT (Non-Fungible Token) collections based on their unique traits.

---

## Features

- **Rarity Calculation**  
  Automatically computes a rarity score for each NFT by analyzing the frequency of its traits within the entire collection.

- **Multiple Sorting Algorithms**  
  Includes implementations of **Quick Sort**, **Merge Sort**, and **Insertion Sort** to rank NFTs from rarest to most common.

- **Collection Management**  
  Supports creating and managing multiple NFT collections independently.

- **Detailed Insights**  
  Allows viewing specific traits and rarity data for each individual NFT.

---

## Technical Overview

### Rarity Score Formula

The application calculates rarity using the statistical **Sum of Rarity** method:

- For each trait, the application determines how many NFTs in the collection share that specific value.
- The rarity of a single trait is calculated as:

1 / (Number_of_NFTs_with_Trait / Total_NFTs)

- The final **Rarity Score** of an NFT is the sum of all its individual trait rarities.

---

### Sorting Logic

To handle large collections efficiently, the project utilizes sorting algorithms, primarily **Quick Sort**:

- **Pivot Selection**: Uses the middle element of the list as the pivot.
- **Ordering**: NFTs are sorted in descending order (highest rarity score first).

---

## Project Structure

model/ -> Data entities such as NFT, Collection, and Trait
service/ -> Core logic for rarity calculation and sorting algorithms
data/ -> Local database handlers for NFT and collection storage
ui/ -> Android Activities and Adapters for the user interface

---

## Getting Started

- **Launch**: The app starts with a splash screen and redirects to the collection list.
- **Add Collection**: Create a new NFT collection (e.g., *Pixel Dogs*).
- **Add NFTs**: Enter NFT names, images, and their unique traits.
- **Rank NFTs**: Trigger the calculation to rank NFTs by rarity.

---

## Installation

1. Clone the repository:
git clone https://github.com/ulasyesilyurt/nftranksorter
2. Open the project with **Android Studio**
3. Sync Gradle files
4. Run the app on an emulator or physical device (**API 24+ recommended**)

---

## License

This project is developed for educational purposes.

