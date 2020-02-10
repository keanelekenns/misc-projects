import numpy as np
import pandas as pd
from sklearn.decomposition import PCA

f = open("adult.test", 'r')
f2= open("adult.data", 'r')

dattype={"Age":int, "Workclass":str, "fnlwgt":int, "Education":str, "Education-num":int, "marital-status":str, "occupation":str, "relationship":str, "race":str, "sex":str, "capital-gain":int, "capital-loss":int, "hours-per-week":int, "native-country":str, "salary":str} 
datnames=np.array(("Age", "Workclass", "fnlwgt", "Education", "Education-num", "marital-status", "occupation", "relationship", "race", "sex", "capital-gain", "capital-loss", "hours-per-week", "native-country", "salary"))

def wcCon(wc):
    wcdict ={"Private":1, "Self-emp-not-inc":2, "Self-emp-inc":3, "Federal-gov":4, "Local-gov":5, "State-gov":6, "Without-pay":7, "Never-worked":8, "?":'?'}
    return wcdict[wc]
    
def eduCon(edu):
    edudict = {"Bachelors":1, "Some-college":2, "11th":3, "HS-grad":4, "Prof-school":5, "Assoc-acdm":6, "Assoc-voc":7, "9th":8, "7th-8th":9, "12th":10, "Masters":11, "1st-4th":12, "10th":13, "Doctorate":14, "5th-6th":15, "Preschool":16, "?":"?"}
    return edudict[edu]

def marCon(mar):
    mardict = {"Married-civ-spouse":1, "Divorced":2, "Never-married":3, "Separated":4, "Widowed":5, "Married-spouse-absent":6, "Married-AF-spouse":7,"?":"?"}
    return mardict[mar]

def ocCon(oc):
    ocdict = {"Tech-support":1, "Craft-repair":2, "Other-service":3, "Sales":4, "Exec-managerial":5, "Prof-specialty":6, "Handlers-cleaners":7, "Machine-op-inspct":8, "Adm-clerical":9, "Farming-fishing":10, "Transport-moving":11, "Priv-house-serv":12, "Protective-serv":13, "Armed-Forces":14, "?":"?"}
    return ocdict[oc]

def relCon(rel):
    reldict = {"Wife":1, "Own-child":2, "Husband":3, "Not-in-family":4, "Other-relative":5, "Unmarried":6, "?":"?"}
    return reldict[rel]

def raceCon(race):
    racedict = {"White":1, "Asian-Pac-Islander":2, "Amer-Indian-Eskimo":3, "Other":4, "Black":5, "?":"?"}
    return racedict[race]
    
def sexCon(sex):
    sexdict = {"Female":-1, "Male":1, "?":"?"}
    return sexdict[sex]
    
def cunCon(cun):
    cundict = {"United-States":1, "Cambodia":2, "England":3, "Puerto-Rico":4, "Canada":5, "Germany":6, "Outlying-US(Guam-USVI-etc)":7, "India":8, "Japan":9, "Greece":10, "South":11, "China":12, "Cuba":13, "Iran":14, "Honduras":15, "Philippines":16, "Italy":17, "Poland":18, "Jamaica":19, "Vietnam":20, "Mexico":21, "Portugal":22, "Ireland":23, "France":24, "Dominican-Republic":25, "Laos":26, "Ecuador":27, "Taiwan":28, "Haiti":29, "Columbia":30, "Hungary":31, "Guatemala":32, "Nicaragua":33, "Scotland":34, "Thailand":35, "Yugoslavia":36, "El-Salvador":37, "Trinadad&Tobago":38, "Peru":39, "Hong":40, "Holand-Netherlands":41, "?":"?"}
    return cundict[cun]
    
def salKhan(sal):
    saldict = {">50K.":-1, "<=50K.":1}
    return saldict[sal]

data = pd.read_csv(f, dtype=dattype, names = datnames, delimiter=", ", na_values='?', converters={"Workclass":wcCon, "Education":eduCon, "marital-status":marCon, "occupation":ocCon, "relationship":relCon, "race":raceCon, "sex":sexCon, "native-country":cunCon, "salary":salKhan})
# data_mask = np.genfromtxt(f2, dtype=None, usemask=True, delimiter=", ", missing_values=b'?')

print(data[13:16])
print(data[-5:])
data.to_csv("adult.test.conv", sep=",")

data = data.dropna()

print(data[13:16])
print(data[-5:])

# data.to_csv("adult.test.clean", sep=",")
data_norm = data.copy()
# data_norm = (data - data.mean())/(data.max() - data.min())
for feature in ("Age", "Workclass", "fnlwgt", "Education", "Education-num", "marital-status", "occupation", "relationship", "race", "capital-gain", "capital-loss", "hours-per-week", "native-country"):
    data_norm[feature] = (data[feature] - data[feature].mean())/(data[feature].max() - data[feature].min())
print(data_norm[13:16])
print(data_norm[-5:])


# data_norm.to_csv("adult.test.norm", sep=",")

data_drop = data.drop(["Workclass", "fnlwgt", "marital-status", "relationship", "capital-loss", "Education-num", "native-country"], axis=1)

print(data_drop[13:16])
print(data_drop[-5:])

# data_drop.to_csv("adult.test.drop", sep=",")

print(np.array(data["salary"]).reshape((1,-1)).shape)
pca = PCA(n_components = 3)
data_pca = np.hstack((pca.fit_transform(data_norm.drop("salary", axis=1)), np.array(data["salary"]).reshape((-1,1))))
print(data_pca[:5], data_pca.shape)

np.savetxt("adult.test.PCA", data_pca, delimiter=",")