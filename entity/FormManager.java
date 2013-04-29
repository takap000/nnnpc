package takap.mods.nnnpc.entity;

import java.util.ArrayList;
import java.util.List;

import takap.mods.nnnpc.utility.Utility;

public class FormManager
{
    private static FormManager instance = new FormManager();
    private List<FormBase> formList;
    
    private FormManager()
    {
        createFormList();
    }
    
    private void createFormList()
    {
        this.formList = new ArrayList<FormBase>();
    }
    
    public static FormManager getInstance()
    {
        return instance;
    }
    
    public void registerForm(FormBase form)
    {
        if ( form.isAvailable() )
        {
            this.formList.add(form);
            form.registerForm();
        }
    }

    public int getNumberOfForms()
    {
        return this.formList.size();
    }

    public FormBase getForm(String name)
    {
        int size = getNumberOfForms();
        for ( int i=0; i<size; i++ )
        {
            if ( this.formList.get(i).getName().equals(name) )
            {
                return formList.get(i);
            }
        }
        return null;
    }

    public FormBase getDefaultForm()
    {
        if ( getNumberOfForms() > 0 )
        {
            return this.formList.get(0);
        }
        return null;
    }

    public int getFormIndex(String name)
    {
        int size = getNumberOfForms();
        int i;
        for ( i=0; i<size; i++ )
        {
            if ( this.formList.get(i).getName().equals(name) )
            {
                return i;
            }
        }
        return -1;
    }

    public FormBase getPreviousForm(String name)
    {
        int index = getFormIndex(name);
        if ( index == -1 )
        {
            return null;
        }
        int size = getNumberOfForms();
        int previousIndex = ((index + size) - 1) % size;
        return this.formList.get(previousIndex);
    }

    public FormBase getNextForm(String name)
    {
        int index = getFormIndex(name);
        if ( index == -1 )
        {
            return null;
        }
        int size = getNumberOfForms();
        int nextIndex = (index + 1) % size;
        return this.formList.get(nextIndex);
    }

    public void showRegisteredFormInformation()
    {
        if ( this.formList.size() <= 0 )
        {
            Utility.printInformation("no Forms are registered.");
        }
        int size = getNumberOfForms();
        for ( int i=0; i<size; i++ )
        {
            Utility.printInformation("=====  " + (i+1) + "th Form information  =====");
            this.formList.get(i).showFormInformation();
            Utility.printInformation("");
        }
    }
}
