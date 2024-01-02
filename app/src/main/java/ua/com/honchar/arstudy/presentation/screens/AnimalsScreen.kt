package ua.com.honchar.arstudy.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.com.honchar.arstudy.presentation.Animal
import ua.com.honchar.arstudy.presentation.ArActivity
import ua.com.honchar.arstudy.presentation.MainViewModel
import ua.com.honchar.arstudy.presentation.getAnimals
import ua.com.honchar.arstudy.ui.theme.ARStudyTheme

@Composable
fun AnimalsScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val animals = getAnimals()
    AnimalsScreenDesc(
        animals = animals,
        onClick = { animal ->
//            viewModel.downloadFile(animal.path, animal.modelName, context) { path ->
                context.startActivity(Intent(context, ArActivity::class.java).apply {
                    putExtra("animal", animal)
//                    putExtra("path", path)
                })
//            }
//            val json = Uri.encode(Gson().toJson(it))
//            navController.navigate(Screen.ARScreen.withArgs(json))
        }
    )
}

@Composable
fun AnimalsScreenDesc(animals: List<Animal>, onClick: (Animal) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 6.dp)
            ) {
                items(animals) { animal ->
                    AnimalCard(
                        animal = animal,
                        onClick = onClick
                    )
                }
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 3.dp
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Next animals will be added in the future",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalCard(animal: Animal, onClick: (Animal) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 9.dp
        ),
        onClick = {
            onClick.invoke(animal)
        },
        modifier = Modifier.padding(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = animal.icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(70.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = animal.name,
                fontSize = TextUnit(16f, TextUnitType.Sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ARStudyTheme {
        AnimalsScreenDesc(getAnimals()) {}
    }
}